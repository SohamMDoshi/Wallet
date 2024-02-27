package currencyconversion

import (
	"context"
	"encoding/json"
	"errors"
	"math"
	"os"
	"path/filepath"
)

type Server struct {
}

type Currency struct {
	Rates map[string]float64 `json:"rates"`
}

func init() {
	// Load conversion rates when package is initialized
	if err := loadConversionRates(); err != nil {
		panic("Failed to load conversion rates: " + err.Error())
	}
}

var currencies Currency
var serviceFeeInINR = 10.0

func loadConversionRates() error {
	// Load conversion rates from JSON file
	filePath := filepath.Join("server", "conversion_rates.json")
	file, err := os.ReadFile(filePath)
	if err != nil {
		return err
	}

	err = json.Unmarshal(file, &currencies.Rates)
	if err != nil {
		return err
	}

	return nil
}

func (s *Server) ConvertCurrency(ctx context.Context, req *ConvertRequest) (*ConvertResponse, error) {
	// Check if currencies are supported
	if _, ok := currencies.Rates[req.BaseCurrency]; !ok {
		return nil, errors.New("base currency not found")
	}
	if _, ok := currencies.Rates[req.TargetCurrency]; !ok {
		return nil, errors.New("target currency not found")
	}

	// Perform currency conversion
	baseRate := currencies.Rates[req.BaseCurrency]
	targetRate := currencies.Rates[req.TargetCurrency]
	convertedAmount := (req.Amount / baseRate) * targetRate
	serviceFee := math.Round((serviceFeeInINR*baseRate)*100) / 100

	// Create and return response
	res := &ConvertResponse{
		ConvertedAmount: convertedAmount,
		Currency:        req.TargetCurrency,
		ServiceFee:      serviceFee,
	}
	return res, nil
}

func (s *Server) mustEmbedUnimplementedConverterServer() {

}

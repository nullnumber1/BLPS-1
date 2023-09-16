#!/bin/bash

read -p "Please enter the host (e.g., http://localhost:8080): " host

echo "Initiating payment..."
response=$(curl -s -X POST $host/payments)
paymentId=$response
echo "Payment ID: $paymentId"
sleep 3

echo "Filling payment type..."
curl -s -X POST $host/payments/$paymentId/type -d 'type=test&amount=100.0'
echo "Payment type filled."
sleep 3

echo "Filling OKTMO..."
curl -s -X POST $host/payments/$paymentId/oktmo -d 'code=12345'
echo "OKTMO filled."
sleep 3

echo "Filling payer and generating payment document..."
curl -s -X POST $host/payments/$paymentId/payer -d 'name=PayerName&INN=1234567890&for_self=true' -o paymentDocument.pdf
sleep 3

echo "Opening PDF..."
open paymentDocument.pdf
sleep 3

echo "Processing payment..."
curl -s -X POST $host/payments/$paymentId/process -d 'payment_method=credit_card'
echo "Payment processed."
sleep 3

echo "Getting payment status..."
response=$(curl -s -X GET $host/payments/$paymentId)
echo "Payment status retrieved:$response"


#!/bin/bash

read -p "Please enter the host (e.g., http://localhost:8080): " host
read -p "Please enter your username: " username
read -s -p "Please enter your password: " password
echo

# Authenticate and get access token
authResponse=$(curl -s -X POST $host/auth/authenticate -H "Content-Type: application/json" -d "{\"username\":\"$username\", \"password\":\"$password\"}")
accessToken=$(echo "$authResponse" | jq -r '.access_token')

echo "Initiating payment..."
response=$(curl -s -X POST $host/payments/init -H "Authorization: Bearer $accessToken")
echo "$response"
paymentId=$response
echo "Payment ID: $paymentId"
sleep 3

echo "Filling payment type..."
response=$(curl -s -X POST $host/payments/$paymentId/type -H "Authorization: Bearer $accessToken" -d 'type=test&amount=100.0')
echo "$response"
echo "Payment type filled."
sleep 3

echo "Filling OKTMO..."
response=$(curl -s -X POST $host/payments/$paymentId/oktmo -H "Authorization: Bearer $accessToken" -d 'code=12345')
echo "$response"
echo "OKTMO filled."
sleep 3

echo "Filling payer and generating payment document..."
response=$(curl -s -X POST $host/payments/$paymentId/payer -H "Authorization: Bearer $accessToken" -d 'name=PayerName&INN=1234567890&for_self=true' -o paymentDocument.pdf)
echo "$response"
echo "Payer details filled and payment document generated."
sleep 3

echo "Opening PDF..."
open paymentDocument.pdf
sleep 3

echo "Processing payment..."
response=$(curl -s -X POST $host/payments/$paymentId/process -H "Authorization: Bearer $accessToken" -d 'payment_method=credit_card')
echo "$response"
echo "Payment processed."
sleep 3

echo "Getting payment status..."
response=$(curl -s -X GET $host/payments/$paymentId -H "Authorization: Bearer $accessToken")
echo "$response"
echo "Payment status retrieved."

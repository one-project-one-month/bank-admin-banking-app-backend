#!/bin/bash

# Database configuration for Corporate Banking App
export DB_URL="jdbc:postgresql://localhost:5432/corporate_banking"
export DB_USERNAME="banking_user"
export DB_PASSWORD="banking_password"

echo "✅ Environment variables set:"
echo "DB_URL=$DB_URL"
echo "DB_USERNAME=$DB_USERNAME"
echo "DB_PASSWORD=***hidden***"

# Run your application with these environment variables
echo ""
echo "🚀 Starting user-service with database connection..."
#!/bin/bash
set -e

# Colors
GREEN='\033[0;32m'
NC='\033[0m'

echo -e "${GREEN}Starting HMS MVP Deployment...${NC}"

# 1. Install Docker if missing
if ! command -v docker &> /dev/null; then
    echo "Installing Docker..."
    curl -fsSL https://get.docker.com | sudo sh
    sudo usermod -aG docker $USER
    echo "Please re-login to pick up docker group permissions." 
    # Warning: This script might fail here if user permissions aren't refreshed.
fi

# 2. Setup Project Directory (Simplification for VM: assumes valid context or git pull)
# In a real scenario, you'd git clone here.
# For this script, we assume files are present or we create the compose file.

echo -e "${GREEN}Building and Starting Services...${NC}"

# 3. Docker Compose Up
# Using the specific MVP compose file
docker compose -f docker-compose-mvp.yml up --build -d

echo -e "${GREEN}Deployment Complete!${NC}"
echo "Backend: http://localhost:8080"
echo "Database: localhost:5433"
echo "Check logs with: docker compose -f docker-compose-mvp.yml logs -f"

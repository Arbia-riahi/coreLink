#!/bin/bash

echo "=========================================="
echo " Starting service readiness check..."
echo "=========================================="

# Default URLs (override via environment variables)
FRONTEND_URL=${FRONTEND_URL:-http://corelink-front:80}
BACKEND_URL=${BACKEND_URL:-http://corelink-back:8080}

# Function to wait for a service to become available
wait_for_service() {
    local url="$1"
    local name="$2"

    echo "Waiting for $name at $url ..."

    until curl -s -o /dev/null "$url"; do
        echo "$name not ready, waiting 5s..."
        sleep 5
    done

    echo "$name is up!"
}

# Wait for backend
wait_for_service "$BACKEND_URL" "Backend"

# Wait for frontend
wait_for_service "$FRONTEND_URL" "Frontend"

echo "=========================================="
echo " Updating test configuration..."
echo "=========================================="

CONFIG_FILE="/app/src/test/resources/config/configFile.properties"

if [ -f "$CONFIG_FILE" ]; then
    sed -i "s|http://localhost:58080|$FRONTEND_URL|g" "$CONFIG_FILE"
    echo "Updated $CONFIG_FILE with new FRONTEND_URL."
else
    echo "WARNING: $CONFIG_FILE not found. Skipping update."
fi

echo "=========================================="
echo " Starting virtual display for headless mode"
echo "=========================================="

export DISPLAY=:99
Xvfb :99 -screen 0 1920x1080x24 &

echo "=========================================="
echo "Running Maven tests (headless)"
echo "=========================================="

mvn test -Dheadless=true
status=$?

echo "=========================================="
echo "Tests completed!"
echo "=========================================="

sleep 120
exit $status

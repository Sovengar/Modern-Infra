set -x
./mvnw clean verify
open -a "Google Chrome" $(pwd)/$(ls -d target/pit-reports/*|head -n 1)/com.jonathan.modern_design/index.html

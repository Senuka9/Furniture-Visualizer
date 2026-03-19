# Download BCrypt JAR
$url = "https://repo1.maven.org/maven2/at/favre/lib/bcrypt/0.10.2/bcrypt-0.10.2.jar"
$output = ".\lib\bcrypt-0.10.2.jar"

Write-Host "Downloading BCrypt JAR from Maven repository..."
$ProgressPreference = 'SilentlyContinue'

try {
    Invoke-WebRequest -Uri $url -OutFile $output -ErrorAction Stop
    if (Test-Path $output) {
        Write-Host "[OK] BCrypt JAR downloaded successfully!"
        Write-Host ""
        Write-Host "Files in lib folder:"
        dir .\lib
    }
} catch {
    Write-Host "[ERROR] Failed to download BCrypt JAR"
    Write-Host "Error: $_"
    Write-Host ""
    Write-Host "Please manually download from:"
    Write-Host $url
    Write-Host ""
    Write-Host "And save to: .\lib\bcrypt-0.10.2.jar"
    exit 1
}

Write-Host ""
Write-Host "Now run: mvn clean install"


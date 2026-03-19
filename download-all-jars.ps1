# Download all missing JARs
$urls = @{
    "bcrypt-0.10.2.jar" = "https://repo1.maven.org/maven2/at/favre/lib/bcrypt/0.10.2/bcrypt-0.10.2.jar"
    "bson-4.11.1.jar" = "https://repo1.maven.org/maven2/org/mongodb/bson/4.11.1/bson-4.11.1.jar"
}

Write-Host "Downloading missing JARs..."
$ProgressPreference = 'SilentlyContinue'

foreach ($jar in $urls.Keys) {
    $output = ".\lib\$jar"
    if (Test-Path $output) {
        Write-Host "[OK] $jar already exists"
    } else {
        $url = $urls[$jar]
        Write-Host "Downloading $jar..."
        try {
            Invoke-WebRequest -Uri $url -OutFile $output -ErrorAction Stop
            Write-Host "[OK] Downloaded $jar"
        } catch {
            Write-Host "[ERROR] Failed to download $jar"
            Write-Host "URL: $url"
        }
    }
}

Write-Host ""
Write-Host "Files in lib folder:"
dir .\lib


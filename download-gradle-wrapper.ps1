# PowerShell script to download the gradle-wrapper.jar file
$url = "https://github.com/gradle/gradle/raw/v8.2.1/gradle/wrapper/gradle-wrapper.jar"
$output = "gradle\wrapper\gradle-wrapper.jar"

Write-Host "Downloading gradle-wrapper.jar from $url"
Invoke-WebRequest -Uri $url -OutFile $output
Write-Host "Downloaded gradle-wrapper.jar to $output"
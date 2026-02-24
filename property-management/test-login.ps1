$uri = "http://localhost:8080/api/user/login"
$headers = @{"Content-Type"="application/json"}
$body = '{"phone":"13800000001","password":"123456"}'

Try {
    $response = Invoke-RestMethod -Uri $uri -Method POST -Headers $headers -Body $body
    Write-Host "状态码: 200"
    Write-Host "响应:"
    $response | ConvertTo-Json -Depth 3
} Catch {
    Write-Host "状态码: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "错误: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "响应体: $responseBody"
        $reader.Close()
    }
}
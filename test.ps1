$body = @{
    email='admin@demo.com'
    password='admin'
}
$jsonBody = $body | ConvertTo-Json
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -Body $jsonBody -ContentType 'application/json'
$token = $response.token

$headers = @{
    Authorization = "Bearer " + $token
}
$notifs = Invoke-RestMethod -Uri 'http://localhost:8080/api/notifications' -Method Get -Headers $headers
$notifs | ConvertTo-Json -Depth 5

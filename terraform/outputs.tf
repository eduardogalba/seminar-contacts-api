output "api_url" {
  description = "URL pública base de la API"
  value       = "https://${azurerm_container_app.api.ingress[0].fqdn}"
}

output "swagger_ui_url" {
  description = "URL directa a la interfaz Swagger UI"
  value       = "https://${azurerm_container_app.api.ingress[0].fqdn}/swagger-ui/index.html"
}
variable "subscription_id" {
  description = "Corresponde a AZURE_SUBSCRIPTION_ID"
  type        = string
}

variable "location" {
  description = "Región de Azure donde tienes tu infraestructura"
  type        = string
  default     = "italynorth"
}

variable "resource_group_name" {
  description = "Corresponde a AZURE_RESOURCE_GROUP"
  type        = string
}

variable "container_app_name" {
  description = "Corresponde a CONTAINER_APP_NAME"
  type        = string
}

variable "docker_image" {
  description = "Imagen inicial en Docker Hub (ej. tuusuario/seminar-api:latest)"
  type        = string
}

variable "db_host" {
  description = "Corresponde a DB_CONTACT_SERVICE_HOST"
  type        = string
}

variable "db_port" {
  description = "Corresponde a DB_CONTACT_SERVICE_PORT"
  type        = string
  default     = "5432"
}

variable "db_name" {
  description = "Corresponde a DB_CONTACT_SERVICE_NAME"
  type        = string
}

variable "db_user" {
  description = "Corresponde a DB_CONTACT_SERVICE_USER"
  type        = string
}

variable "db_password" {
  description = "Corresponde a DB_CONTACT_SERVICE_PASSWORD"
  type        = string
  sensitive   = true
}
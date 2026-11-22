terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "azurerm" {
  features {}
  subscription_id = var.subscription_id
}

resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_container_app_environment" "env" {
  name                = "cae-${var.container_app_name}"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
}

resource "azurerm_container_app" "api" {
  name                         = var.container_app_name
  container_app_environment_id = azurerm_container_app_environment.env.id
  resource_group_name          = azurerm_resource_group.main.name
  revision_mode                = "Single"

  secret {
    name  = "db-contact-service-password"
    value = var.db_password
  }

  template {
    min_replicas = 1
    max_replicas = 3

    container {
      name   = "seminar-api"
      image  = var.docker_image
      cpu    = 0.5
      memory = "1Gi"

      env {
        name  = "DB_CONTACT_SERVICE_HOST"
        value = var.db_host
      }
      env {
        name  = "DB_CONTACT_SERVICE_PORT"
        value = var.db_port
      }
      env {
        name  = "DB_CONTACT_SERVICE_NAME"
        value = var.db_name
      }
      env {
        name  = "DB_CONTACT_SERVICE_USER"
        value = var.db_user
      }
      env {
        name        = "DB_CONTACT_SERVICE_PASSWORD"
        secret_name = "db-contact-service-password"
      }
    }
  }

  ingress {
    external_enabled = true
    target_port      = 8080
    traffic_weight {
      latest_revision = true
      percentage      = 100
    }
  }

  # Evita que Terraform revierta el tag SHA desplegado por GitHub Actions
  lifecycle {
    ignore_changes = [
      template[0].container[0].image
    ]
  }
}
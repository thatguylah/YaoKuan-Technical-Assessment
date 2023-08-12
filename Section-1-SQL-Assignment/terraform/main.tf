provider "aws" {
  region = "ap-southeast-1" # Change to your preferred AWS region
  access_key = "fake_access_key" # Usually use this for one and done deployments (Not advised)
  secret_key = "fake_Secret_key" # Really dont recommend storing secret values in code. (Just showing this for completeness.)
}

resource "aws_security_group" "allow_postgresql" {
  name        = "allow_postgresql"
  description = "Allow inbound traffic for PostgreSQL"

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Caution: This allows traffic from anywhere. You might want to narrow this down.
  }
}

resource "aws_db_instance" "postgres_db" {
  allocated_storage    = 20   # Modify as needed
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "13" # Latest version as of my last update, but you should check
  instance_class       = "db.t2.micro" # Choose a different instance type if needed

  name                 = "mydatabase"
  username             = "myuser"
  password             = "mypassword"

  parameter_group_name = "default.postgres13"
  skip_final_snapshot  = true

  vpc_security_group_ids = [aws_security_group.allow_postgresql.id]
}


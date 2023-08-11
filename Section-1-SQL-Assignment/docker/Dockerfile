FROM postgres:latest

# Set environment variables (you can change these if necessary)
ENV POSTGRES_USER myuser
ENV POSTGRES_PASSWORD mypassword
ENV POSTGRES_DB mydatabase

# Copy the DDL script to the docker entrypoint directory to execute it on container start
COPY DDL.sql /docker-entrypoint-initdb.d/

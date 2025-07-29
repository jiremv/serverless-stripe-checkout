#!/bin/bash

REPO="jiremv/serverless-stripe-checkout"

echo "Estableciendo secretos en el repositorio $REPO..."

# Secrets de entorno DEV
gh secret set TESTING_STACK_NAME -b"serverless-stripe-checkout-dev" -r "$REPO"
gh secret set TESTING_REGION -b"us-west-1" -r "$REPO"
gh secret set TESTING_ARTIFACTS_BUCKET -b"aws-sam-cli-managed-serverless-str-artifactsbucket-ybnudupemh9u" -r "$REPO"
gh secret set TESTING_CLOUDFORMATION_EXECUTION_ROLE -b"arn:aws:iam::545200407638:role/aws-sam-cli-managed-serve-CloudFormationExecutionRo-ELbmYygqZJAA" -r "$REPO"
gh secret set STRIPE_API_KEY_ARN_DEV -b"arn:aws:secretsmanager:us-west-1:545200407638:secret:stripe-api-key-5CaMij" -r "$REPO"
gh secret set STRIPE_WEBHOOK_SECRET_ARN_DEV -b"arn:aws:secretsmanager:us-west-1:545200407638:secret:stripe-webhook-secret-NC6v1d" -r "$REPO"

# Secrets de entorno PROD
gh secret set PROD_STACK_NAME -b"serverless-stripe-checkout-prod" -r "$REPO"
gh secret set PROD_REGION -b"us-west-2" -r "$REPO"
gh secret set PROD_ARTIFACTS_BUCKET -b"aws-sam-cli-managed-serverless-str-artifactsbucket-mx0cgnmg1mck" -r "$REPO"
gh secret set PROD_CLOUDFORMATION_EXECUTION_ROLE -b"arn:aws:iam::545200407638:role/aws-sam-cli-managed-serve-CloudFormationExecutionRo-Uz1d30qGw3S6" -r "$REPO"
gh secret set STRIPE_API_KEY_ARN_PROD -b"arn:aws:secretsmanager:us-west-2:545200407638:secret:stripe-api-key-fuABZu" -r "$REPO"
gh secret set STRIPE_WEBHOOK_SECRET_ARN_PROD -b"arn:aws:secretsmanager:us-west-2:545200407638:secret:stripe-webhook-secret-VbiIoK" -r "$REPO"

echo "✅ Todos los secrets fueron establecidos exitosamente."

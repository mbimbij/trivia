#!/bin/bash
npx openapi-generator-cli generate -i ../openapi.yaml -g typescript-angular \
  -o src/app/openapi-generated --additional-properties fileNaming=kebab-case,withInterfaces=true \
  --generate-alias-as-model

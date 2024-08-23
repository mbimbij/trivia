#!/bin/bash

# game API contract
npx openapi-generator-cli generate -i ../openapi.game.yaml -g typescript-angular \
  -o src/app/openapi-generated/game --additional-properties fileNaming=kebab-case,withInterfaces=true \
  --generate-alias-as-model

# game logs API contract
npx openapi-generator-cli generate -i ../openapi.gamelogs.yaml -g typescript-angular \
  -o src/app/openapi-generated/gamelogs --additional-properties fileNaming=kebab-case,withInterfaces=true \
  --generate-alias-as-model

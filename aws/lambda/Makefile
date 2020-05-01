#!/usr/bin/make -f

SHELL = /bin/bash

.PHONY: build clean invoke apply plan destroy refresh output

all: build
LAMBDA_FUNCTION := 'guacamole-config'


define BUILD_TERRAFORM_ARGS
VAR_FILE_ARGS="" && \
for VAR_FILE in $$(find . -name "*.tfvars"); \
do VAR_FILE_ARGS+=" -var-file=$${VAR_FILE}"; \
done
endef

build:
	npm install && \
	mkdir -p ./output && \
	./node_modules/.bin/webpack && \
	cd ./output && \
	zip -9 -q -r ./guacamole-config.zip ./index.js

clean:
	rm -rf ./node_modules ./output

invoke:
	aws lambda invoke --function-name ${LAMBDA_FUNCTION} /dev/stdout

plan:
	terraform init && \
	$(call BUILD_TERRAFORM_ARGS) && \
	terraform plan $${VAR_FILE_ARGS}

apply:
	terraform init && \
	$(call BUILD_TERRAFORM_ARGS) && \
	terraform apply $${VAR_FILE_ARGS} -auto-approve

destroy:
	terraform init && \
	$(call BUILD_TERRAFORM_ARGS) && \
	terraform destroy $${VAR_FILE_ARGS} -auto-approve

refresh:
	terraform init && \
	$(call BUILD_TERRAFORM_ARGS) && \
	terraform refresh $${VAR_FILE_ARGS}

output: refresh
	terraform output


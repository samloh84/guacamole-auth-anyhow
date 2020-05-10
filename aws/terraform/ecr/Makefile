SHELL := /bin/bash
.SHELLFLAGS := -ec

.PHONY: build push save load rmi run rund run_shell exec_shell kill logs logsf rm start stop

TERRAFORM_TFVARS_FILE_PATTERNS := *.tfvars

define _LOAD_AWSRC :=
	if [[ -f "~/.awsrc" ]]; then \
		source "~/.awsrc"; \
	fi; \
	if [[ -f "$(CURDIR)/.awsrc" ]]; then \
		source "$(CURDIR)/.awsrc";  \
	fi
endef

define _SET_TERRAFORM_VAR_FILE_ARGS :=
	if [[ ! -z "$(TERRAFORM_TFVARS_FILE_PATTERNS)" ]]; then \
		TERRAFORM_TFVARS_FILE_PATTERNS=($(TERRAFORM_TFVARS_FILE_PATTERNS)); \
	else \
		TERRAFORM_TFVARS_FILE_PATTERNS=(); \
	fi; \
	TERRAFORM_VAR_FILE_ARGS=(); \
	for TERRAFORM_TFVARS_FILE_PATTERN in $${TERRAFORM_TFVARS_FILE_PATTERNS[@]}; do \
		for TERRAFORM_TFVARS_FILE in $$(find $(CURDIR) -name "$${TERRAFORM_TFVARS_FILE_PATTERN}"); do \
			TERRAFORM_VAR_FILE_ARGS+=("-var-file" "$${TERRAFORM_TFVARS_FILE}"); \
		done; \
	done
endef


all: apply push

apply:
	set -euxo pipefail; \
	$(call _LOAD_AWSRC); \
	$(call _SET_TERRAFORM_VAR_FILE_ARGS); \
	TERRAFORM_APPLY_ARGS=("-auto-approve" "$${TERRAFORM_VAR_FILE_ARGS[@]}"); \
	terraform init; \
	terraform apply "$${TERRAFORM_APPLY_ARGS[@]}"

destroy:
	set -euxo pipefail; \
	$(call _LOAD_AWSRC); \
	$(call _SET_TERRAFORM_VAR_FILE_ARGS); \
	TERRAFORM_DESTROY_ARGS=("-auto-approve" "$${TERRAFORM_VAR_FILE_ARGS[@]}"); \
	terraform init; \
	terraform destroy "$${TERRAFORM_DESTROY_ARGS[@]}"

refresh:
	set -euxo pipefail; \
	$(call _LOAD_AWSRC); \
	$(call _SET_TERRAFORM_VAR_FILE_ARGS); \
	TERRAFORM_REFRESH_ARGS=("$${TERRAFORM_VAR_FILE_ARGS[@]}"); \
	terraform init; \
	terraform refresh "$${TERRAFORM_REFRESH_ARGS[@]}"

output:
	set -euxo pipefail; \
	$(call _LOAD_AWSRC); \
	terraform init; \
	terraform output


guacamole_client_docker_image:
	set -euxo pipefail; \
	$(MAKE) -C ../../../docker/guacamole-client

push: guacamole_client_docker_image
	set -euxo pipefail; \
	$(call _LOAD_AWSRC); \
	AWS_ACCOUNT_ID="$$(aws sts get-caller-identity | jq -r .Account)"; \
	ECR_REPOSITORY_URL="$$(terraform output ecr_repository_url)"; \
	aws ecr get-login-password --region ap-southeast-1 | \
	docker login --username AWS --password-stdin $${AWS_ACCOUNT_ID}.dkr.ecr.ap-southeast-1.amazonaws.com; \
	docker tag registry.gitlab.com/govtech-iacwg/guacamole-auth-anyhow/guacamole-client:latest $${ECR_REPOSITORY_URL}:latest; \
	docker push $${ECR_REPOSITORY_URL}:latest

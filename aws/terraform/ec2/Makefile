SHELL := /bin/bash
.SHELLFLAGS := -ec

.PHONY: build push save load rmi run rund run_shell exec_shell kill logs logsf rm start stop

TERRAFORM_TFVARS_FILE_PATTERNS := *.tfvars

define _LOAD_AWSRC :=
	if [[ -f "$${HOME}/.awsrc" ]]; then \
		source "$${HOME}/.awsrc"; \
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

define _SET_CENTOS_WORKLOAD_PRIVATE_IPS :=
	TERRAFORM_OUTPUT_PROJECT=$$(terraform output project); \
	TERRAFORM_OUTPUT_OWNER=$$(terraform output project); \
	CENTOS_WORKLOAD_IPS=($$(aws ec2 describe-instances \
	--filter "Name=tag:Name,Values=$${TERRAFORM_OUTPUT_PROJECT}-centos-workload" \
	--filter "Name=tag:Project,Values=$${TERRAFORM_OUTPUT_PROJECT}" \
	--filter "Name=tag:Owner,Values=$${TERRAFORM_OUTPUT_OWNER}" \
	 | jq -r .Reservations[].Instances[].PrivateIpAddress ))
endef

define _SET_CENTOS_WORKLOAD_PRIVATE_IPS :=
	TERRAFORM_OUTPUT_PROJECT=$$(terraform output project); \
	TERRAFORM_OUTPUT_OWNER=$$(terraform output project); \
	WINDOWS_WORKLOAD_IPS=($$(aws ec2 describe-instances \
	--filter "Name=tag:Name,Values=$${TERRAFORM_OUTPUT_PROJECT}-windows-workload" \
	--filter "Name=tag:Project,Values=$${TERRAFORM_OUTPUT_PROJECT}" \
	--filter "Name=tag:Owner,Values=$${TERRAFORM_OUTPUT_OWNER}" \
	 | jq -r .Reservations[].Instances[].PrivateIpAddress ))
endef

define _SET_GUACAMOLE_JUMPHOST_PUBLIC_IPS :=
	TERRAFORM_OUTPUT_PROJECT=$$(terraform output project); \
	TERRAFORM_OUTPUT_OWNER=$$(terraform output owner); \
	GUACAMOLE_JUMPHOST_PUBLIC_IPS=($$(aws ec2 describe-instances \
	--filters "Name=instance-state-name,Values=running" \
	"Name=tag:Name,Values=$${TERRAFORM_OUTPUT_PROJECT}-guacamole-jumphost" \
	"Name=tag:Project,Values=$${TERRAFORM_OUTPUT_PROJECT}" \
	"Name=tag:Owner,Values=$${TERRAFORM_OUTPUT_OWNER}" \
	 | jq -r .Reservations[].Instances[].PublicIpAddress )); \
	 echo "$${GUACAMOLE_JUMPHOST_PUBLIC_IPS[@]}"
endef

define _SET_SSH_KEY_NAME :=
	SSH_KEY_NAME="$$(terraform output key_name)"
endef

all: apply

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

ssh_jumphost:
	set -euxo pipefail; \
	$(call _LOAD_AWSRC); \
	$(call _SET_GUACAMOLE_JUMPHOST_PUBLIC_IPS); \
	$(call _SET_SSH_KEY_NAME); \
	SSH_ARGS=(); \
	if [[ ! -z "$${SSH_KEY_NAME}" ]]; then \
	  	if [[ -f "$${HOME}/.ssh/$${SSH_KEY_NAME}" ]]; then \
	  		SSH_ARGS+=("-i" "$${HOME}/.ssh/$${SSH_KEY_NAME}"); \
		elif [[ -f "$${HOME}/.ssh/$${SSH_KEY_NAME}.key" ]]; then \
			SSH_ARGS+=("-i" "$${HOME}/.ssh/$${SSH_KEY_NAME}.key"); \
		elif [[ -f "$${HOME}/.ssh/$${SSH_KEY_NAME}.pem" ]]; then \
			SSH_ARGS+=("-i" "$${HOME}/.ssh/$${SSH_KEY_NAME}.pem"); \
  		fi; \
	fi; \
	SSH_ARGS+=("centos@$${GUACAMOLE_JUMPHOST_PUBLIC_IPS[0]}"); \
	SSH_ARGS+=("-o StrictHostKeyChecking=no"); \
	ssh "$${SSH_ARGS[@]}"


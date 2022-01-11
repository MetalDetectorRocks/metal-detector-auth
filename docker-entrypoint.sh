#!/bin/bash

file_env() {
	local var="$1"
	local fileVar="${var}_FILE"
	local def="${2:-}"
	if [ "${!var:-}" ] && [ "${!fileVar:-}" ]; then
		echo >&2 "error: both $var and $fileVar are set (but are exclusive)"
		exit 1
	fi
	local val="$def"
	if [ "${!var:-}" ]; then
		val="${!var}"
	elif [ "${!fileVar:-}" ]; then
		val="$(< "${!fileVar}")"
	fi

	export "$var"="$val"
	unset "$fileVar"
}

envs=(
  METAL_DETECTOR_USER_CLIENT_ID
  METAL_DETECTOR_USER_CLIENT_SECRET
  METAL_DETECTOR_ADMIN_CLIENT_ID
  METAL_DETECTOR_ADMIN_CLIENT_SECRET
  IMPORT_JOB_CLIENT_ID
  IMPORT_JOB_CLIENT_SECRET
  AUTHORIZATION_SERVER_PRIVATE_KEY
  AUTHORIZATION_SERVER_PUBLIC_KEY
  DATASOURCE_USERNAME
  DATASOURCE_PASSWORD
  DATASOURCE_URL
  ACTUATOR_INFO_PATH
  ACTUATOR_FLYWAY_PATH
  ACTUATOR_METRICS_PATH
  ACTUATOR_PROMETHEUS_PATH
)

for e in "${envs[@]}"; do
  file_env "$e"
done

exec "$@"

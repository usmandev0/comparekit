#!/usr/bin/env bash
set -euo pipefail

RAW="$(printf '%s' "${SIGNING_KEY_ID:-}" | tr -d ' \n\r\t' | sed 's/^0x//I')"
HEX="$(printf '%s' "$RAW" | tr -cd '0-9A-Fa-f')"

if [ -z "$HEX" ]; then
  echo "::error::SIGNING_KEY_ID is empty or contains no hexadecimal characters."
  echo "Run locally: gpg --list-secret-keys --keyid-format=long"
  exit 1
fi

case ${#HEX} in
  8)
    SHORT_KEY_ID="$HEX"
    ;;
  16|40)
    SHORT_KEY_ID="${HEX: -8}"
    ;;
  *)
    echo "::error::SIGNING_KEY_ID must be a GPG short key ID (8), long key ID (16), or fingerprint (40)."
    echo "After cleanup it has ${#HEX} hex characters."
    echo "Run locally: gpg --list-secret-keys --keyid-format=long"
    exit 1
    ;;
esac

SHORT_KEY_ID="$(printf '%s' "$SHORT_KEY_ID" | tr '[:lower:]' '[:upper:]')"
echo "Using short key ID: $SHORT_KEY_ID"
echo "ORG_GRADLE_PROJECT_signingInMemoryKeyId=$SHORT_KEY_ID" >> "$GITHUB_ENV"

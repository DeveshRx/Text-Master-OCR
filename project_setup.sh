
# Generate app.env file
echo $APP_ENV_FILE_BASE64  > app.env.b64
base64 -d app.env.b64  >> app.env

# Generate sign key file
echo $SIGN_KEY_BASE64 > key.b64
base64 -d key.b64 >> key.jks

# Generate google-services.json file
echo $GOOGLE_SERVICE_JSON_BASE64 > gsjd.b64
base64 -d gsjd.b64 >> /home/circleci/project/app/google-services.json

# Generate GOOGLE_APPLICATION_CREDENTIALS file
echo $GOOGLE_APPLICATION_CREDENTIALS_BASE64 > gac.b64
base64 -d gac.b64 >> /home/circleci/project/GOOGLE_APPLICATION_CREDENTIALS.json




















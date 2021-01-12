#生成密钥
keytool -genkeypair -alias jwt -validity 3650 -keyalg RSA -dname "CN=jwt,OU=jtw,O=jtw,L=zurich,S=zurich,C=CH" -keypass 123456 -keystore F:/jwt/jwt.jks -storepass 123456
#生成公钥
keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey


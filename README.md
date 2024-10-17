# Building the code and running the tests
```
./mvnw clean install
```

# Running the code
```
./mvwn spring-boot:run
```

### Sample request
```
curl --location 'http://localhost:8080/parcelcost?weight=50&height=10&width=10&length=10&voucher=TESTVOUCHER'
```

### Sample successful response
```
{
    "status": "success",
    "body": 995.0
}
```

### Sample error response
```
{
    "status": "error",
    "body": "Parcel weight exceeds 50.0kg"
}
```

# Configuration file
```
parcelcost.properties
```

Update the property `voucher.service.url` to the voucher API endpoint

Update the property `voucher.service.apikey` to a valid API key


# Assumptions / TODO
- Move the parcel cost and weight factors to a Database table so it can be updated in runtime
- Secure the `voucher.service.apikey` property by encrypting it or using Secrets Manager or something similar
- Voucher discount is subtraction, not percentage
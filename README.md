




Branching strategy

1. master

2. develop_8.743.20.0
3. release_8.743.20.0

Branches related to 8.743.20.0 release

* task/BO-1012-implement-entity-structure
* task/BO-1013-implement-login-endpoint



Here I have used java 17 new feature; enhanced switch statement

how OTP for logging should handle.

1. since we are using stateless rest apis and keeping logging details in the db is a cost
2. we can handle that using FE.
3. we can maintain lastLoginTime in browser localstorage.
if its not available or time gap from now is 3h, generate the token after going through otp process.

1. trigger otp generation after user enter the correct username and password. response should not return the token

2. Need endpoint to enter otp and get token.
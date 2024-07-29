package com.company.module.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.company.module.utils.MapperUtils;
import com.company.module.exception.ErrorCode;
import com.company.module.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseJWT {

	public static final String SUB = "sub";
	private final String ROLES = "roles";
	private final String EMAIL = "email";
	private final String CUSTODY_ID = "custodyID";
	private final String SESSION_ID = "sessionID";
	private final String OTP = "otp";
	private final String OTP_TYPE = "otpType";
	public static final String JWT_PAYLOAD = "jwt_payload";
	public static final String AUTHORIZATION = "Authorization";
	public static final String API_KEY = "X-Api-Key";
	private NativeWebRequest webRequest;
	private Map<String, Claim> claims;
	private List<String> myRoles;
	private String userid;
	private String email;
	private String custodyCode;
	private String nativeToken;
	private String token;
	private String apiKey;
	private DecodedJWT jwt;
	private DecodedJWT jwtAPI;
	private String sessionID;
	private String otp;
	private String otpType;

	public BaseJWT(NativeWebRequest webRequest) {
		super();
		this.webRequest = webRequest;
	}

	public List<String> getRolesFromJWT() {
		try {
			if (myRoles == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.myRoles = claims.get(ROLES).asList(String.class);
				return this.myRoles;
			}
			return this.myRoles;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BaseException(e, ErrorCode.PROCESSING_JWT_ERROR);
		}
	}

	public String getUseridFromJWT() {
		try {
			if (this.userid == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.userid = claims.get(SUB).asString();
				log.info("Got userid from jwt:{}", userid);
			}
			return userid;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BaseException(e, ErrorCode.PROCESSING_JWT_ERROR);
		}
	}

	public String getEmailLFromJWT() {
		try {
			if (this.email == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.email = claims.get(EMAIL).asString();
				log.info("Got email from jwt");
			}
			return email;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BaseException(e, ErrorCode.PROCESSING_JWT_ERROR);
		}
	}

	public String getCustodyCodeFromJWT() {
		try {
			if (this.custodyCode == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.custodyCode = claims.get(CUSTODY_ID).asString();
				log.info("Got custodyCode from jwt");
			}
			return custodyCode;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BaseException(e, ErrorCode.PROCESSING_JWT_ERROR);
		}
	}

	public Map<String, Claim> getClaimsFromJwt() {
		if (this.claims == null) {
			DecodedJWT jwt = getJwt();
			this.claims = jwt.getClaims();
		}
		return this.claims;
	}

	private DecodedJWT getJwt() {
		if (this.jwt == null) {
			this.jwt = JWT.decode(getToken());
		}
		return this.jwt;
	}

	private String getToken() {
		if (this.token == null) {
			String nativeToken = getNativeToken();
			this.token = nativeToken.substring(7, nativeToken.length());
		}
		return this.token;
	}

	public String getNativeToken() {
		if (!StringUtils.hasLength(this.nativeToken)) {
			this.nativeToken = webRequest.getHeader(AUTHORIZATION);
			if(!StringUtils.hasLength(this.nativeToken)) {
				String payload = webRequest.getHeader(JWT_PAYLOAD);
				if(StringUtils.hasLength(payload)) {
					String token = createToken(payload);
					this.nativeToken = "Bearer " + token;
				}
			}
		}
		//log.info("Found token");
		return this.nativeToken;
	}

	public boolean isExistJwt() {
		if (StringUtils.isEmpty(getNativeToken())) {
			return false;
		} else {
			return true;
		}
	}

	public void validateUseridAndJwt(String userid) {
		String useridFromJWT = getUseridFromJWT();
		if (!userid.equals(getUseridFromJWT())) {
			throw new BaseException(ErrorCode.PROCESSING_JWT_ERROR,
					new StringBuilder("userid ").append(userid).append(" not equal ")
							.append(" userid From JWT ").append(useridFromJWT));
		}
	}

	public String getApiKey() {
		if (this.apiKey == null) {
			this.apiKey = webRequest.getHeader(API_KEY);
		}
		return this.apiKey;
	}

	public DecodedJWT validateApiKey() {
		String apiKey = getApiKey();
		if (StringUtils.isEmpty(apiKey)) {
			throw new BaseException(ErrorCode.PROCESSING_APIKEY_ERROR, new StringBuilder("ApiKey not found"));
		}

		try {
			this.jwtAPI = JWT.decode(apiKey);
			return this.jwtAPI;
		} catch (JWTDecodeException e) {
			throw new BaseException(e, ErrorCode.PROCESSING_APIKEY_ERROR, new StringBuilder("ApiKey can't decode"));
		}
	}

	public String getSessionID() {
		try {
			if (this.sessionID == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.sessionID = claims.get(SESSION_ID)!=null?claims.get(SESSION_ID).asString():"";
			}
			return sessionID;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public String getOtp() {
		try {
			if (this.otp == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.otp = claims.get(OTP)!=null?claims.get(OTP).asString():"";
			}
			return otp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public String getOtpType() {
		try {
			if (this.otpType == null) {
				Map<String, Claim> claims = getClaimsFromJwt();
				this.otpType = claims.get(OTP_TYPE)!=null?claims.get(OTP_TYPE).asString():"";
			}
			return otpType;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public String createToken(String payload) {
		try {
			Claims claims = Jwts.claims()
					.add(MapperUtils.readValue(payload, HashMap.class))
					.build();
			return Jwts.builder()
					.claims(claims)
					.compact();
		} catch (Exception ex) {
			log.error("Error createToken: ", ex);
			throw new BaseException(ErrorCode.PROCESSING_JWT_ERROR,
					new StringBuilder("createToken error"));
		}
	}
}

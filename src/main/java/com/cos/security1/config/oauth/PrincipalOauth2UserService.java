package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	//구글로부터 받은 userRequest 데이터에 대한  후처리 되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		System.out.println("userRequest:"+userRequest);
		System.out.println("getClientRegistration:"+userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인 했는지 확인가능.
		System.out.println("getAccessToken:"+userRequest.getAccessToken());
		
		OAuth2User oauth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(Oauth-Client라이브러리)->AccessToken요청
		// userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받아준다.
		System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes());
				
		//회원가입을 강제로 진행예정
		String provider = userRequest.getClientRegistration().getClientId(); //google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId;	//"google_111061868356786625939"
		String email = oauth2User.getAttribute("email");		
		String role = "ROLE_USER";

		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.email(email)
					.password("겟인데어")
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어있습니다.");
		}
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}

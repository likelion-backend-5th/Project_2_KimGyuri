# 미션형 프로젝트[2] 📮멋사SNS📮

> **미션을 바탕으로 나만의 멋사 SNS를 만드는 것이 목표입니다.**
<br>

## 📑 프로젝트 요구사항

<details>
<summary>사용자 인증 요구사항</summary>

### ✉ 사용자 인증
```
1. 사용자 회원가입이 가능하다.
    - 회원가입에 필수로 필요한 정보는 아이디와 비밀번호 이다.
    - 부수적으로 이메일, 전화번호를 기입할 수 있다.
    
2. 아이디와 비밀번호를 통해 로그인이 가능하다.
    - 인증 방식은 JWT를 이용한 토큰 인증 방식을 택한다.
    
3. 로그인 한 상태에서, 자신을 대표하는 사진, 프로필 사진을 업로드 할 수 있다.
```
</details>

<details>
<summary>피드 구현 요구사항</summary>
  
### ✉ 피드 구현
```
1. 피드는 제목과 내용을 붙일 수 있다.
    - 피드에는 복수의 이미지를 넣을 수 있다.
    
2. 피드를 작성하고자 한다면 로그인 된 상태여야 한다.
    - 사용자가 피드를 작성하면, 특별한 설정 없이 자신이 작성한 피드로 등록된다.
    
3. 피드는 작성한 사용자 기준으로, 목록 형태의 조회가 가능하다.
    - 조회를 위해 대상 사용자의 정보가 제공되어야 한다.
    - 피드 목록 조회시, 작성자 아이디, 제목과 대표 이미지에 관한 정보가 포함되어야 한다.
    - 이때 대표 이미지란 피드에 등록된 첫번째 이미지를 의미한다.
    - 만약 피드에 등록된 이미지가 없다면, 지정된 기본 이미지를 보여준다.
    
4. 피드는 단독 조회가 가능하다.
    - 피드 단독 조회시, 피드에 연관된 모든 정보가 포함되어야 한다. 이는 등록된 모든 이미지를 확인할 수 있는 각각의 URL과, 댓글 목록, 좋아요의 숫자를 포함한다.
    - 피드를 단독 조회할 시, 로그인이 된 상태여야 한다.
    
5. 피드는 수정이 가능하다.
    - 피드에 등록된 이미지의 경우, 삭제 및 추가만 가능하다.
    - 피드의 이미지가 삭제될 경우 서버에서도 해당 이미지를 삭제하도록 한다.
    
6. 피드는 삭제가 가능하다.
    - 피드가 삭제될때는 실제로 데이터베이스에서 삭제하는 것이 아닌, 삭제 되었다는 표시를 남기도록 한다.
```
</details>

<details>
<summary>댓글, 좋아요 구현 요구사항</summary>
  
### ✉ 댓글, 좋아요 구현
#### 댓글
```
1. 댓글 작성은 로그인 한 사람만 쓸 수 있다.
    - 댓글에는 작성자 아이디, 댓글 내용이 포함된다.
2. 자신이 작성한 댓글은 수정 및 삭제가 가능하다.
    - 댓글이 삭제될때는 실제로 데이터베이스에서 삭제하는 것이 아닌, 삭제 되었다는 표시를 남기도록 한다.
3. 댓글의 조회는 피드의 단독 조회와 함께 이뤄진다.
```

#### 좋아요
```
1. 다른 사용자의 피드는 좋아요를 할 수 있다.
    - 자신의 피드의 좋아요는 할 수 없다(권한 없음).
    - 좋아요 요청을 보낼 때 이미 좋아요 한 상태라면, 좋아요는 취소된다
```
</details>

<details>
<summary>사용자 정보 구현 요구사항</summary>

### ✉ 사용자 정보 구현
```
1. 사용자의 정보는 조회가 가능하다.
    - 이때 조회되는 정보는 아이디와 프로필 사진이다.
    
2. 로그인 한 사용자는 다른 사용자를 팔로우 할 수 있다.
    - 팔로우는 일방적 관계이다. A 사용자가 B를 팔로우 하는 것이 B 사용자가 A를 팔로우 하는것을 의미하지 않는다.
    
3. 로그인 한 사용자는 팔로우 한 사용자의 팔로우를 해제할 수 있다.

4. 로그인 한 사용자는 다른 사용자와 친구 관계를 맺을 수 있다.
    - 친구 관계는 양방적 관계이다. A 사용자가 B와 친구라면, B 사용자와 A 도 친구이다.
    - A 사용자는 B 사용자에게 친구 요청을 보낸다.
    - B 사용자는 자신의 친구 요청 목록을 확인할 수 있다.
    - B 사용자는 친구 요청을 수락 혹은 거절할 수 있다.
    
5. 사용자의 팔로우한 모든 사용자의 피드 목록을 조회할 수 있다.
    - 이때 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회한다.
    - 그 외 조회되는 데이터는 피드 목록 조회와 동일하다.
    
6. 사용자와 친구관계의 모든 사용자의 피드 목록을 조회할 수 있다.
    - 이때 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회한다.
    - 그 외 조회되는 데이터는 피드 목록 조회와 동일하다.
```
</details>
<br>

## 📆 프로젝트 진행 상황
<details>
<summary>📬 사용자 인증</summary>

### 23.08.03
> - 회원 가입 기능 구현 : 아이디와 비밀번호는 필수 입력 항목, 그 외 선택. 비밀번호와 비밀번호체크가 일치해야 회원가입 가능
> - JWT Token 발급 기능(로그인) 구현 : JwtTokenUtils - Jwt Token 생성, 유효성 판단, 비밀키 생성, 번역기 생성. TokenController - Token 발급
> - JWT Token 기반 사용자 인증 처리(로그인) 구현 - JwtTokenFilter - 헤더 Bearer로 시작하는지 확인 & 사용자 인증 정보 생성 및 SecurityContext에 사용자 정보 설정 후 SecurityContextHolder에 SecurityContext 설정
> - 프로필 사진 업로드 기능 구현 : Multiparfile

### 23.08.07
> - ResponseBody에 원하는 메시지만 반환되게끔 RuntimeException클래스를 상속받아 각각의 클래스를 구현하는 방법으로 예외 처리 구현
</details>

<details>
<summary>📬 피드</summary>

### 23.08.04
> - Article : ArticleImages = 1 : N 관계 설정
> - Article : User = N : 1 관계 설정

### 23.08.06
> - 로그인한 사용자 피드 등록 기능 구현 : 로그인한 사용자만 등록 가능. 제목과 내용은 필수. 이미지 등록 여부는 선택 가능. 다수의 이미지 등록 가능
> - 사용자 피드 조회 기능 구현 : 로그인하지 않은 사용자도 조회 가능. 등록된 이미지가 없는 글의 경우 basic.jpg(기본 이미지)로 대표 이미지 대체. 등록한 이미지가 있는 경우 글의 첫 번째 이미지가 대표 이미지
> - 피드 단독 조회 기능 구현: 로그인한 사용자만 조회 가능 (게시자, 제목, 내용, 이미지) / 나머지 항목 추후 추가 예정
> - 피드 수정 기능 구현 : 제목 수정, 내용 수정, 이미지 추가(다수가능), 이미지 삭제(다수가능) 가능. 각 항목 수정 여부 선택 가능
> - 피드 삭제 기능 구현 : 피드 삭제 시 deletedAt에 현재 날짜 및 시간 기록. 서버에서 삭제 X
> - 피드 삭제 시 사용자 피드 조회 + 피드 단독 조회에 조회되지 않도록 수정

### 23.08.07
> - 피드 단독 조회 기능 : 해당 피드에 달린 모든 댓글 조회 추가 구현
> - 피드 단독 조회 기능 : 해당 피드에 좋아요된 숫자 추가 구현

### 23.08.08
> - ResponseBody에 원하는 메시지만 반환되게끔 RuntimeException클래스를 상속받아 각각의 클래스를 구현하는 방법으로 예외 처리 구현
> - 피드 단독 조회 기능 : 삭제된 댓글이 보이는 오류 수정
> - 피드 삭제 기능 : 삭제 권한 예외 처리 수정
> - 피드 등록 시 필수 입력 항목 유효성 검사 + 예외 처리 추가
</details>

<details>
<summary>📬 댓글, 좋아요</summary>

### 23.08.07
#### 댓글
> - Comment : Article = N : 1 관계 설정
> - Comment : User = N : 1 관계 설정
> - 댓글 등록 기능 구현 : 로그인한 사용자만 등록 가능
> - 댓글 수정 기능 구현
> - 댓글 삭제 기능 구현 : 피드 삭제 시 deletedAt에 현재 날짜 및 시간 기록. 서버에서 삭제 X
> - 피드 단독 조회 기능에 댓글 조회 기능 추가 구현
#### 좋아요
> - Like : Article = N : 1 관계 설정
> - Like : User = N : 1 관계 설정
> - 좋아요/좋아요취소 기능 구현 : 특정 게시글에 대해 요청을 보내면 좋아요. 이미 좋아요된 상태라면 좋아요 취소.
> - 피드 단독 조회 기능에 좋아요 수 추가 구현

### 23.08.08
#### 댓글
> - ResponseBody에 원하는 메시지만 반환되게끔 RuntimeException클래스를 상속받아 각각의 클래스를 구현하는 방법으로 예외 처리 구현
> - 해당 댓글의 articleId가 아니더라도 댓글이 수정, 삭제되는 오류 수정
> - 댓글 등록/수정 시 필수 입력 항목에 대해 Jakarta Bean Validation API를 사용하여 비어있지 않도록 사용자 입력 유효성 검증
#### 좋아요
> - ResponseBody에 원하는 메시지만 반환되게끔 RuntimeException클래스를 상속받아 각각의 클래스를 구현하는 방법으로 예외 처리 구현
</details>

<details>
<summary>📬 사용자 정보</summary>

### 23.08.07
> - 사용자 정보 조회 기능 구현 : 사용자 아이디(username), 사용자 프로필 사진 조회 가능. 등록된 프로필 사진이 없을 경우 basicProfile.jpg(기본 프로필)로 프로필 이미지 대체.
> - 팔로우/팔로우해제 기능 구현 : 특정 사용자에 대해 요청을 보내면 팔로우. 이미 팔로우된 상태라면 팔로우 해제
> - 친구 요청 기능 구현
> - 친구 요청 목록 조회 기능 구현 : 친구 요청을 보낸 모든 사용자의 아이디(username) 조회 가능
> - 친구 요청 수락/거절 기능 구현 : 수락 시 user_friends의 accepted가 true로 변경 + accepted가 true이면서 from_user와 to_user가 반대인 데이터 생성 / 거절 시 해당 요청 데이터 삭제
> - 팔로우한 모든 사용자의 피드 목록 조회 기능 구현 : 로그인한 사용자가 팔로워로서 팔로잉한 모든 사용자들의 모든 피드 조회 (최신글이 가장 위로 오도록 정렬)

### 23.08.08
> - 중복으로 팔로우되는 오류(팔로우 해제가 되지 않는 오류) 수정
> - 친구관계의 모든 사용자의 피드 목록 조회 기능 구현 : 로그인한 사용자의 친구 관계에 있는 모든 사용자들의 모든 피드 조회 (최신글이 가장 위로 오도록 정렬)
> - ResponseBody에 원하는 메시지만 반환되게끔 RuntimeException클래스를 상속받아 각각의 클래스를 구현하는 방법으로 예외 처리 구현
</details>
<br>



## 💻 API 문서
https://documenter.getpostman.com/view/28055519/2s9XxzuYFa


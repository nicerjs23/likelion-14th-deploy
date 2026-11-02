# spring-deploy-v1 — 6주차 배포 세션 시작 상태 (5주차 완료 + 5주차 과제)

4주차 완료 프로젝트에 5주차 `Spring Security & JWT` 교안의 Part 1~7 코드를 적용하고, **5주차 과제(필수 2 댓글 작성자 검사, 선택 1 내 정보·내 글)** 까지 반영한 프로젝트입니다.
6주차 배포 세션 자료(08)를 **이 폴더에서 처음부터**(GitHub 레포 만들기 → 사전준비 → 배포) 따라 하기 위한 사본이며, 자바 패키지·클래스 이름은 멘티 프로젝트와 같은 `com.likelion.springsession` 을 유지합니다.

## 실행 준비

IntelliJ 실행 설정의 환경변수에 다음 두 값을 등록합니다.

```text
DB_PASSWORD=<현재 MySQL의 likelion 계정 비밀번호>
JWT_SECRET=likelion14th-spring-session-jwt-secret-key-2026
```

`JWT_SECRET`은 HS256 요구사항에 맞게 32바이트 이상이어야 합니다.

## 기존 데이터가 있을 때

이번 주에는 `posts`와 `comments`에 `author_id NOT NULL` 컬럼이 추가됩니다. 작성자가 없는 기존 행이 있으면 `ddl-auto: update`가 정상적으로 스키마를 바꾸지 못할 수 있습니다.

교안대로 학습용 DB를 초기화하려면 다음 순서로 진행합니다.

1. `application.yaml`의 `ddl-auto`를 한 번만 `create`로 변경합니다.
2. 애플리케이션을 실행해 테이블이 생성되는지 확인합니다.
3. 즉시 `ddl-auto: update`로 되돌립니다.
4. 회원가입부터 다시 진행합니다.

운영 데이터가 있는 환경에서는 이 방식을 사용하면 안 됩니다.

## 구현된 API

| 기능 | Method | URL | 인증 |
| --- | --- | --- | --- |
| 회원가입 | POST | `/api/auth/signup` | 불필요 |
| 로그인 | POST | `/api/auth/login` | 불필요 |
| 토큰 재발급 | POST | `/api/auth/reissue` | Refresh Token |
| 로그아웃 | POST | `/api/auth/logout` | Access Token |
| 게시글 목록·상세 | GET | `/api/posts`, `/api/posts/{postId}` | 불필요 |
| 게시글 작성 | POST | `/api/posts` | Access Token |
| 게시글 수정·삭제 | PUT, DELETE | `/api/posts/{postId}` | 작성자 Access Token |
| 댓글 목록 | GET | `/api/posts/{postId}/comments` | 불필요 |
| 댓글 작성 | POST | `/api/posts/{postId}/comments` | Access Token |
| 댓글 수정·삭제 | PUT, DELETE | `/api/posts/{postId}/comments/{commentId}` | 작성자 Access Token (5주차 과제) |
| 내 정보 | GET | `/api/users/me` | Access Token (5주차 선택 과제) |
| 내 글 목록 | GET | `/api/posts/my` | Access Token (5주차 선택 과제) |

## Postman

- `postman/Spring Security Part 2.postman_collection.json`: 회원가입만 확인
- `postman/Spring Security JWT Full.postman_collection.json`: 로그인, 게시글 인가, **댓글 작성자 검사(과제), 내 정보·내 글(선택 과제)**, 재발급, 로그아웃까지 18개 요청 확인. 컬렉션 변수 `baseUrl` 만 바꾸면 배포 서버에도 그대로 실행 가능

같은 DB 에 두 번째 실행하면 1·3번 회원가입은 409(이미 가입됨)가 정상이고 나머지는 동일합니다.

전체 컬렉션은 로그인 응답의 토큰과 생성한 게시글 ID를 컬렉션 변수에 자동으로 저장합니다. 위에서 아래 순서대로 실행하면 됩니다.

## DB 확인

```sql
SELECT id, email, nickname, password FROM users;
SELECT id, title, author_id FROM posts;
SELECT id, content, post_id, author_id FROM comments;
SELECT * FROM refresh_tokens;
```

## 5주차 과제 반영 내용

- **필수 2 — 댓글 작성자 검사**: `CommentAccessDeniedException`(403) 추가, `CommentService.updateComment/deleteComment` 에 `comment.isAuthor(userId)` 검사, `CommentController` 에 `@AuthenticationPrincipal Long userId`. "해당 게시글의 댓글인지" 검사(`belongsTo`)는 그대로 유지.
- **선택 1 — 내 정보·내 글**: `GET /api/users/me`(`UserController`/`UserService`/`UserResponse`), `GET /api/posts/my`(`PostRepository.findAllByAuthor`, `PostService.getMyPosts`). `/api/posts/my` 는 `GET /api/posts/**` permitAll 규칙보다 **먼저** `authenticated()` 로 선언해 토큰 없이는 401.
- 선택 2(ADMIN 역할)는 반영하지 않았습니다.

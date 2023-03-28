# duranno

+ 주요 기능

1. 동영상

![a](https://user-images.githubusercontent.com/103418153/227925596-b1e79446-897b-4910-aefa-6a1aa8dc711b.gif)

##

2. 공지사항

![a2-min](https://user-images.githubusercontent.com/103418153/227926466-e2401caa-c2e7-4d74-bdcd-4e3c6728d944.gif)

 2-1. 관리자의 경우에만 공지사항 등록 버튼 존재
 ```
 <div th:if="${roleType == 'admin'}">
  <a th:href="@{/board}" class="button">등록</a>
 </div>
 ```

##

3. 기수별 자료실

![a3](https://user-images.githubusercontent.com/103418153/227928426-300d2bb2-1457-4a74-941c-69f7e0e4b88b.gif)
 
  3-1. 관리자는 모든 기수의 자료실에 접근이 가능하고, 일반 사용자는 자신의 기수의 자료실에만 접근 가능
  ``` 
  @GetMapping
  public String assignment(@Login SessionValue sessionValue,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String ordinal,
                           Model model) {
      if(sessionValue.getRoleType() == RoleType.ADMIN) {
        if (ordinalCheck(ordinal)) {
          throw new NoSuchElementException("존재하지 않는 기수입니다.");
        }
        getPagination(ordinal + "th", page, model); // 관리자의 경우 쿼리 파라미터가 첫 번째 전달인자가 됨.
        model.addAttribute("roleType", "admin");
      } else {
        getPagination(sessionValue.getOrdinal(), page, model); // 일반 사용자의 경우 세션 값이 첫 번째 전달인자가 됨.
        model.addAttribute("roleType", "user");
      }
  }
  ```
  3-2. 과제/교재의 Id값의 검증 및 쿼리 파라미터의 검증 
  ```
   Assignment findAssignment = assignmentService.findById(assignmentId);

   //과제 Id가 존재하지 않을 경우 기수별 과제 파일 화면으로 redirect
   if (findAssignment == null) {
       return "redirect:/assignment?ordinal=" + ordinal;
   }

   String cmp = findAssignment.getOrdinal().getOrdinal().split("th")[0];

   //과제의 기수가 맞지 않을 경우 기수별 과제 파일 화면으로 redirect
   if (!cmp.equals(ordinal)) {
       return "redirect:/assignment?ordinal=" + ordinal;
   }
  ```


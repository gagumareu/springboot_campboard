## 개발 목표
캠핑 커뮤티니 웹 **개인 프로젝트** 
- 유저가 등록한 캠핑 장비를 리스트업
- 리스트업한 장비들에서 선택하여 중고 거래 작성
- list.html 한 페이지에서 카테고리 별로 리스트 출력 함으로써 최소한의 코드로 원하는 리스트와 검색기능
- 검색기능 '카테고리 내에서 원하는 키워드 검색가능'
- Spring Security를 이용하여 권한이 주어진 회원만 게시물 등록, 수정 및 삭제
- text edition 'summernote edition' 적용

## 사용기술
- Spring boot 2.7.x
- JPA
- Querydsl
- Spring security
- thymeleaf
- Ajax 
- summernote editor
- Google OAuth2
- AWS RDS
- AWS S3 Bucket

사용 언어 : Java, HTML, CSS, Jquery, SQL

버전 관리 : Git 

## 개선 및 원하는 업데이트 
- 회원 프로파일 이미지 수정
- 오픈 소스 map API 이요하여 캠핑 장소 모집
- 회원 이름 클릭시 회원 정보 보이기

------
### 로그인 유저가 등록한 캠핑 리스트 및 중고거래 게시판 작성 ###
![마이기어리스트 중고거래-min](https://github.com/gagumareu/springboot_campboard/assets/98436199/a2bbd943-460e-4f9a-ab7d-0b5fdab6f762)

### summernote editor ###

![게실물등록-min](https://github.com/gagumareu/springboot_campboard/assets/98436199/3e6ceb36-2144-4806-8c78-244be9701cbd)


    <textarea id="summernote" placeholder="CONTENT" name="content"></textarea>
    <script>
        $(document).ready(function() {
    
            var targetImageList = [];
    
            $('#summernote').summernote({
                height: 600,
                lang: "ko-KR",
                callbacks:{
                    onImageUpload: function (files, editor, welEditable){
                        for (var i = files.length -1; i >= 0; i--){
                            console.info(files[i]);
                            uploadImageFiles(files[i], this);
                        } //for
                    }, // onImageUpload
                    onMediaDelete: function (target){
    
                        var fileName = target.attr('src');
    
                        fileName = fileName.substring(fileName.lastIndexOf("/") +1);
    
                        var targetS3 = target.attr('src');
    
                        console.log("fileName: " + fileName);
                        console.log("targetS3: " + targetS3);
    
                        targetImageList.forEach(function (i){
    
                            console.log(targetImageList);
                            console.log(i);
    
                            var targetName = i;
    
                            if (i == targetS3){
    
                                var targetLI = $("li[name='"+fileName+"']");
    
                                targetLI.remove();
    
                                $.ajax({
                                    url: '/removeS3',
                                    data: {files: fileName},
                                    dataType: 'text',
                                    type: 'delete',
                                    success: function (result){
                                    }
                                })
                            }
                        }); // targetImageList forEach
                    } // onMediaDelete
                } // callbacks
            }); // summernote
    
            function uploadImageFiles(files, el){
    
                var formData = new FormData();
    
                formData.append("uploadFiles", files);
    
                $.ajax({
                    url: '/uploadAjax',
                    data: formData,
                    dataType: 'json',
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    success:function (result){
                        console.info(result);
                        uploadResultUL(result);
                    },
                    error: function (jqXHR, textStatus, errorThrown){
                        console.info(textStatus);
                    }
                }); // ajax
            } // function uploadImageFiles
    
            function uploadResultUL(result){
    
                var uploadResultUL = $(".uploadResultUL ul");
                var str = "";
    
                $(result).each(function (i, dto){
    
                    str += '<li data-path="'+dto.folderPath+'" ' +
                        'data-uuid="'+dto.uuid+'" ' +
                        'data-name="'+dto.fileName+'" ' +
                        'name="'+dto.uuid+'_'+dto.fileName+'" data-s3Url="'+dto.s3Url+'"></li>';
    
                    // var imageURL = dto.imageURL;
                    //
                    // console.log("imageURL: " + imageURL);
    
                    $('#summernote').summernote('insertImage', dto.s3Url);
                    // $('#summernote').summernote('insertImage', '/display?files='+dto.imageURL);
    
                    var s3Url = dto.s3Url;
                    console.log(s3Url)
    
                    targetImageList.push(s3Url);
                    console.log(targetImageList);
    
                }); // result each
    
                uploadResultUL.append(str);
    
            }; // function uploadResultUL
    
        }); // the end
    </script>

### 게시물 등록 클릭 이벤트 Jquery ###
  
    $(".registerBtn").on("click", function (e){
    
        e.preventDefault();
    
        console.log("clicking");
    
        var str = "";
    
        $(".uploadResultUL li").each(function (i, dto){
    
            var target = $(dto);
    
            str += '<input type="hidden" name="boardImageDTOList['+i+'].folderPath" value="'+target.data("path")+'">';
            str += '<input type="hidden" name="boardImageDTOList['+i+'].uuid" value="'+target.data("uuid")+'">';
            str += '<input type="hidden" name="boardImageDTOList['+i+'].fileName" value="'+target.data("name")+'">';
            str += '<input type="hidden" name="boardImageDTOList['+i+'].s3Url" value="'+target.data("s3url")+'">';
    
        });
    
        $(".hiddenBox").html(str);
        $("form").submit();
    
    }); // register btn clicking event

##
    


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

사용 언어 : Java, HTML, CSS, Jquery, SQL

버전 관리 : Git 

## 개선 및 업데이트 예정
- 회원 마이 페이지
- 게시물 스크렙 기능
- 회원 프로파일 이미지
- 게시물 리스트 정렬 방식(필터)
- 소셜 로그인

------

### summernote editor ###
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

- 게시물 등록 클릭 이벤트 Jquery
  
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

  ---
    


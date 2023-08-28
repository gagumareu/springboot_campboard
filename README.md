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

##### 로그인 유저[spring security authentication 권한을 가진 사용자] 본인의 캠핑 장비 목록을 리스트업 할 수 있음 #####
##### 캠핑 장비 등록 DTO와 이미지 List를 함께 등록 -> 등록된 장비들은 getJSON을 통해 리스트업 [페이징] -> 등록된 장비들을 수정 및 삭제 가능 #####
##### 원하는 장비를 중고거래로 등록하기 클릭시 -> 해당 장비의 id값을 전달 -> 게시물 등록 페이지에서 spring security authentication 권한을 이용하여 사용자의 foregin key를 참고하여 등록한 장비 리스트업 (ajax) 또한 전달 받은 id값을 통해 summernoteEditor에 해당 장비 값 및 이미지 자동 업로드 #####
##### 원하는 장비를 바꾸고 싶으면 캠핑 장비 리스트에서 원하는 장비를 클릭시 자동으로 summernoteEditor로 해당 자료 업데이트 #####

![마이기어리스트 중고거래-min](https://github.com/gagumareu/springboot_campboard/assets/98436199/a2bbd943-460e-4f9a-ab7d-0b5fdab6f762)



// 기어리스트 박스 업로드 function
        if (checkCategory == 'secondHands'){

            loadGearBoxes(1);

            function loadGearBoxes(page){

                console.log("page: " + page)

                var testing = "junghwan";

                $.getJSON('/gear/pagination/' + email +"/"+ page, function (data){

                    console.log(data.dtoList);
                    console.log(data);
                    // 기어리스트 DTO 업로드
                    showGearBoxes(data.dtoList);
                    // pagination 업로드
                    makingPagination(data);
                })
            }
        }

        // 기어리스트 DTO 업로드
        function showGearBoxes(dtoList){

            var gearList = $(".gear-data-boxes");
            var str = "";

            console.info(dtoList);

            $.each(dtoList, function (i, dto){

                str += '<div class="col-md-2 my-gear-li" data-gno="'+dto.gno+'">';

                str += '<div class="loadGear-img-box">';
                if (dto.gearImageDTOList[0] !== null){
                    str += "<img src='"+dto.gearImageDTOList[0]?.thumbnailS3URL+"'>";
                }else {
                    str += "<img src='/icon/img3.svg' style='height: 100%'>";
                }
                str += '</div>';

                str += '<div class="gear-info">';
                str += '<span>이름: '+dto.gname+'</span><span>브랜드: '+dto.brand+'</span><span>사이즈: '+dto.size+'</span><span>소재: '+dto.material+'</span>';
                str += '<span class="gear-script">설명: '+dto.script+'</span><span>카테고리: '+dto.sort+'</span>';
                str += '</div>';

                str += '</div>';


            }) // dtoList each

            gearList.html(str);
        }

        // pagination 업로드
        function makingPagination(data){

            var gearPageUl = $(".pagination");

            var str = "";

            $(data).each(function (i, dto){

                console.log(dto);

                $.each(dto.pageList, function (i, page){
                    str += '<li class="page-link">';
                    str += '<a href="'+page+'">'+page+'</a>';
                    str += '</li>';
                })

            }) // pageList each

            gearPageUl.html(str);

        } //makingPagination

        var page = 1;

        // pagination 클릭시 이베트
        $(".list-pagination ul").on("click", "a", function (e){

            e.preventDefault();

            console.log("clicking");

            var targetPage = $(this).attr("href");

            page = targetPage;

            console.log(page);

            loadGearBoxes(page);

        })


        // 기어선택 클릭 이벤트
        $(".gear-data-boxes").on("click", ".my-gear-li", function (){

            $('#summernote').summernote('reset');

            var gno = $(this).data("gno");

            console.info(gno);

            var str =""

            var hiddenGearBox = $(".hidden-gear-box");

            str += '<input type="hidden" name="gno" value="'+gno+'">';
            str += '<input type="hidden" name="state" value="1">';

            hiddenGearBox.html(str);


            $.getJSON('/gear/images/' + gno, function (arr){

                console.info("---------/gear/images/'----------");

                console.info(arr);

                $.each(arr, function (i, dto){

                    if (dto !== null){
                        $('#summernote').summernote('insertImage', dto.s3Url);
                    }else {
                        console.info("there are no avalable images");
                    }
                });

            }); // getJSON for images

            $.getJSON('/gear/myGear/' + gno, function (dto){

                var name = dto.gname;
                var brand = dto.brand;
                var size = dto.size;
                var material = dto.material;
                var script = dto.script;
                var sort = dto.sort;
                var state = dto.state;

                console.info(name);
                console.info(brand);
                console.info(size);
                console.info(material);
                console.info(script);
                console.info(sort);
                console.info(state);

                // var HTMLString = '<div><p>이름: '+name+'</p><p>브랜드: '+brand+'</p><p>사이즈: '+size+'</p>' +
                //     '<p>소재: '+material+'</p><p>설명: '+script+'</p><p><br></p></div>'

                var HTMLString = '<div><p>'+name+'</p><p>'+brand+'</p><p>'+size+'</p>' +
                    '<p>'+material+'</p><p>'+script+'</p><p><br></p></div>'

                $('#summernote').summernote('pasteHTML', HTMLString);

            }); // getJSON for gear info

        }); // 기어선택 클릭 이벤트 종료


        var gearDTO = [[${gearDTO}]];
        var secondHandsDeal = [[${tellCategory}]];
        console.log(gearDTO)

        // 선택된 중고 상품 editor로 업로드 from myGear
        if(gearDTO !== null){

            var gno = gearDTO.gno;
            console.log(gno);

            $.getJSON('/gear/myGear/' + gno, function (dto){

                var name = dto.gname;
                var brand = dto.brand;
                var size = dto.size;
                var material = dto.material;
                var script = dto.script;
                var sort = dto.sort;
                var state = dto.state;

                console.info(name);
                console.info(brand);
                console.info(size);
                console.info(material);
                console.info(script);
                console.info(sort);
                console.info(state);

                var HTMLString = '<div><p>'+name+'</p><p>'+brand+'</p><p>'+size+'</p>' +
                    '<p>'+material+'</p><p>'+script+'</p><p><br></p></div>'

                $('#summernote').summernote('pasteHTML', HTMLString);

            }); // getJSON

            $.getJSON('/gear/images/' + gno, function (arr){

                console.info("---------/gear/images/'----------");

                console.info(arr);

                $.each(arr, function (i, dto){

                    if (dto !== null){
                        $('#summernote').summernote('insertImage', dto.s3Url);
                    }else {
                        console.info("there are no avalable images");
                    }
                });

            }); // getJSON for images

            var str = "";

            var hiddenGearBox = $(".hidden-gear-box");

            str += '<input type="hidden" name="gno" value="'+gno+'">';
            str += '<input type="hidden" name="state" value="1">';

            hiddenGearBox.html(str);

        } // load gearDTO from myGear




### summernote editor ###

##### summernotEditor에서 이미지 선택 -> [ajax] 로컬 서버 파일 저장 -> AWS S3 bucket 저장 및 로컬 서버 파일 삭제 -> summernoteEditor 이미지 보여주기 -> 게시물 등록 버튼 클릭 -> 게시물 DTO 및 다중 이미지 파일 List와 함께 controller -> service -> repository -> AWS RDS DB 저장 #####

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
    


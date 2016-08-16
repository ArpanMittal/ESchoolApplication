@extends('layouts.app')
@extends('layouts.navigation')
@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <div class="panel panel-default">
                    <div class="panel-heading">Question</div>
                    <div class="panel-body">
                        <div class="widget-body">
                            <div class="container-fluid">
                                <div class="row-fluid">
                                    <div class="span9">
                                        <form class="form-horizontal" action="{{ url('/question/add') }}" method="POST" enctype="multipart/form-data" id="frmquestion"   >
                                            {{ csrf_field() }}
                                            <input class="span6" type="hidden"  name="TeacherId"   id="TeacherId"   style="width:95%;"  value="{{$user->id}}" >
                                            <h5>Create Question </h5>
                                            <hr>
                                            <div class="control-group">
                                                <label class="control-label">Class & Subject</label>
                                                <div class="controls">
                                                    <select name="SubjectId" id="SubjectId"  class="span6"   style="width:250px;" onchange="loadChapters()">
                                                        <option value="">-- Select --</option>
                                                        @if(isset($subjects))
                                                            @foreach($subjects as $subject)
                                                                    <option value="{{ $subject->cl_su_id }}"> {{$subject->class_name ." - ". $subject->subject_name}}</option>
                                                            @endforeach
                                                        @endif
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Chapter</label>
                                                <div class="controls">
                                                    <select name="ChapterId" id="ChapterId"  class="span6"   style="width:250px;" onchange="loadTopics()">
                                                        @if(isset($chapters))
                                                            @foreach($chapters as $chapter)
                                                                <option value="{{ $chapter->cl_su_st_ch_id }}"> {{$chapter->chapter_name}}</option>
                                                            @endforeach
                                                        @endif
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Topic</label>
                                                <div class="controls">
                                                    <select name="TopicId" id="TopicId"  class="span6"   style="width:250px;">
                                                        @if(isset($topics))
                                                            @foreach($topics as $topic)
                                                                <option value="{{ $topic->hash }}"> {{$topic->topic_name}}</option>
                                                            @endforeach
                                                        @endif
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Question Type</label>
                                                <div class="controls">
                                                    <select id="QuesType" name="QuesType" class="span6" >
                                                        @if(isset($types))
                                                            @foreach($types as $type)
                                                                <option value="{{ $type->id }}" > {{$type->question_type }}</option>
                                                            @endforeach
                                                        @endif
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Question</label>
                                                <div class="controls" id="ckeditor">
                                                    @if(isset($question))
                                                        <input name="QuestionId" type="hidden" value="{{$question->id}}"/>
                                                    @endif
                                                    <textarea id="question" name="question"></textarea>
                                                </div>
                                                <div class="control-group">
                                                    <label class="control-label">Question Diagram</label>
                                                    <div class="controls" >
                                                        @if(isset($question))
                                                            <input class="" type="hidden" name="question_diagram" id="question_diagram" hidden="" />
                                                            <input class="" type="button" name="question_diagram_button" id="question_diagram_button" value="Change Image" />
                                                            <img src="{!! asset($question->image_path) !!}" />
                                                        @else
                                                            <input class="" type="file" name="question_diagram" id="question_diagram"  />
                                                        @endif
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="optionsObj">
                                                <div class="control-group">
                                                    <label class="control-label">Options</label>
                                                    @if(isset($question) && $options)
                                                        <div class="controls">
                                                            @for($i=0;$i<count($options);$i++)
                                                                <input name="OptId[]"  type="hidden"  value="{{$options[$i]->id}}">
                                                            @endfor
                                                        </div>
                                                    @endif
                                                    <div class="controls" id="opt1">
                                                        <input name="Opt[]" id="Option1" type="text" placeholder="Option 1" value="">
                                                        <label class="radio inline">
                                                            <input type="radio" class="opt" id="rad1" name="optionsRadioG" id="inlineRadioA" value="1">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="control-group" id="opt2" >
                                                    <div class="controls">
                                                        <input name="Opt[]" id="Option2" type="text" placeholder="Option 2" value="">
                                                        <label class="radio inline">
                                                            <input type="radio" class="opt" id="rad2" name="optionsRadioG" id="inlineRadioB" value="2" >
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="control-group" id="opt3">
                                                    <div class="controls">
                                                        <input name="Opt[]" id="Option3" type="text" placeholder="Option 3" value="">
                                                        <label class="radio inline">
                                                            <input type="radio" class="opt" id="rad3" name="optionsRadioG" id="inlineRadioB" value="3">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="control-group">
                                                    <div class="controls" id="opt4" >
                                                        <input name="Opt[]" id="Option4" type="text" placeholder="Option 4" value="">
                                                        <label class="radio inline">
                                                            <input type="radio" class="opt" id="rad4" name="optionsRadioG" id="inlineRadioB"  value="4">
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Solution</label>
                                                <div class="controls" >
                                                    @if(isset($question))
                                                        <input class="" type="hidden" name="solution" id="solution" hidden="" />
                                                        <input class="" type="button" name="solution_button" id="solution_button" value="Change Image" />
                                                        <img src="{!! asset($question->solution_path) !!}" />
                                                    @else
                                                        <input class="" type="file" name="solution" id="solution"  />
                                                    @endif
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">Ideal Time</label>
                                                <div class="controls" >
                                                    <input class="" type="time" name="ideal_time" id="ideal_time" value="00:01:30" step="1"/>
                                                </div>
                                            </div>
                                            @if(!isset($question))
                                            <div class="control-group">
                                                <label class="control-label">Level</label>
                                                <div class="controls" >
                                                    <select name="Level" id="Level" style="width:220px;">
                                                        <option value="">-- Select Level of Question --</option>
                                                        <option value="2">Easy</option>
                                                        <option value="5" selected="selected">Medium</option>
                                                        <option value="8">Hard</option>
                                                    </select>
                                                </div>
                                            </div>
                                            @endif
                                            <div class="form-actions">
                                                @if(isset($question))
                                                    <button type="button" class="btn btn-info" id="nextButton" name="nextButton" >Next</button>
                                                @endif
                                                <button type="submit" class="btn btn-info" id="epSave" name="epSave">Save</button>
                                                <button type="reset" id="cancleForm" class="btn">Cancel</button>
                                            </div>
                                        </form>
                                        <div id="output"  hidden="hidden"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection

@section('script')
    <script src="{!! asset('/vendor/unisharp/laravel-ckeditor/ckeditor.js') !!}"></script>
    <script src="http://malsup.github.com/jquery.form.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            CKEDITOR.replace("question");
            CKEDITOR.replace("Option1", { toolbar : 'OptionVersion',
                width :   '500px',
                height: '50px' });
            CKEDITOR.replace("Option2", { toolbar : 'OptionVersion',
                width :   '500px',
                height: '50px' });
            CKEDITOR.replace("Option3", { toolbar : 'OptionVersion',
                width :   '500px',
                height: '50px' });
            CKEDITOR.replace("Option4", { toolbar : 'OptionVersion',
                width :   '500px',
                height: '50px' });
            @if(isset($question))
                selectQuestion();
            @endif
        });

        @if(isset($question))
        function selectQuestion(){
            $('#SubjectId').val('{{$selected_subject}}');
            $('#ChapterId').val('{{$selected_chapter}}');
            $('#TopicId').val('{{$question->hash}}');
            $('#QuesType').val({{$question->question_type_id}});

            var ques = `{{$question->question}}`;
            CKEDITOR.instances.question.setData(ques);
            @for($i=0;$i<count($options);$i++)
                @foreach($answer as $ans)
                @if($options[$i]->id == $ans->answer)
                    $('#rad{{$i+1}}').prop('checked', true);
                @endif
                @endforeach
        CKEDITOR.instances.Option{{$i+1}}.setData('{{$options[$i]->opt}}');
            @endfor
            document.getElementById("ideal_time").value = '{{$question->ideal_attempt_time}}';
        }

        $("#question_diagram_button").click(function() {
            $('#question_diagram').attr({type:"file"});
            $('#question_diagram').trigger('click');
            $('#question_diagram_button').attr({type:"hidden"});
        });
        $("#solution_button").click(function() {
            $('#solution').attr({type:"file"});
            $('#solution').trigger('click');
            $('#solution_button').attr({type:"hidden"});
        });
        $("#nextButton").click(function() {
            window.location = '{{ url('/question/next?QuestionId='.$question->id) }}';
        });
        @endif

        $("#frmquestion").submit(function(){
            var question = CKEDITOR.instances.question.getData();
            if (question=="") {
                alert("Please fill question");
                return false;
            }

            var opt1 = CKEDITOR.instances.Option1.getData();
            var opt2 = CKEDITOR.instances.Option2.getData();
            var opt3 = CKEDITOR.instances.Option3.getData();
            var opt4 = CKEDITOR.instances.Option4.getData();
            if (opt1=='' || opt2=='' || opt3=='' || opt4=='') {
                alert("Please fill all options");
                return false;
            }
            $("#question").val(question);
            $("#Option1").val(opt1);
            $("#Option2").val(opt2);
            $("#Option3").val(opt3);
            $("#Option4").val(opt4);
            var formData = new FormData($(this)[0]);

            $.ajax({
                @if(isset($question))
                    url: '{{ url('/question/update') }}',
                @else
                    url: '{{ url('/question/add') }}',
                @endif
                type: 'POST',
                data: formData,
                async: false,
                success: function (data) {
                    data = JSON.parse(data);
                    if(data.success=='false'){
                        alert(JSON.stringify(data.error));
                    }else {
                        alert('saved');
                        @if(!isset($question))
                        clearFields();
                        @endif
                    }
                },
                cache: false,
                contentType: false,
                processData: false
            });

            return false;
        });

        function clearFields() {
            CKEDITOR.instances.question.setData('');
            CKEDITOR.instances.Option1.setData('');
            CKEDITOR.instances.Option2.setData('');
            CKEDITOR.instances.Option3.setData('');
            CKEDITOR.instances.Option4.setData('');

            $('#question_diagram').val('');
            $('#solution').val('');
            $('#ideal_time').val('00:00');
        }

        $("#cancleForm").click(function() {
            window.location = "{{ url('/question/list') }}";
        });

        $('#frmquestion').validate({
            errorClass:'myClass',

            rules: {
                "TeacherId":{ required: true},
                "SubjectId":{ required: true},
                "ChapterId":{ required: true},
                "TopicId":{ required: true},
                "QuesType":{required: true},
                "question":{required: true},
                "Opt":{required: true},
                "optionsRadioG":{required: true},
                "ideal_time":{required: true},
                "Level":{required: true}
            },

            messages: {
                "TeacherId":{ required: "Please reload cannot get your id"},
                "SubjectId":{ required: "Please select Class and Subject"},
                "ChapterId":{ required: "Please select a chapter"},
                "TopicId":{ required: "Please select a topic"},
                "QuesType":{required: "Please select Question Type"},
                "question":{required: "Please insert question"},
                "Opt":{required: "Please fill all options"},
                "ideal_time":{required: "Please fill ideal time"},
                "optionsRadioG":{required: "Please select the correct answer to this question"},
                "Level":{required: "Please select question level"}
            }
        });

        function loadTopics()
        {
            var chapterId=$("#ChapterId").val();
            $('#TopicId')
                    .find('option')
                    .remove()
                    .end();
            if(chapterId!=""){
                var dataToSend="ChapterId="+chapterId;
                $.ajax({
                    url:'{{ url('/question/topic/list') }}',
                    type:'GET',
                    data: dataToSend,
                    success:function(response) {
                        $('#TopicId').append('<option value="">-- Select --</option>');
                        response.forEach(function (item, index) {
                            $('#TopicId').append('<option value="'+item.hash+'">'+item.topic_name+'</option>');
                        });
                    }
                });
            }
        }

        function loadChapters()
        {
            var subjectId=$("#SubjectId").val();
            $('#ChapterId')
                    .find('option')
                    .remove()
                    .end();
            $('#TopicId')
                    .find('option')
                    .remove()
                    .end();
            if(subjectId!=""){
                var dataToSend="SubjectId="+subjectId;
                $.ajax({
                    url:'{{ url('/question/chapter/list') }}',
                    type:'GET',
                    data: dataToSend,
                    success:function(response) {
                        $('#ChapterId').append('<option value="">-- Select --</option>');
                        response.forEach(function (item, index) {
                            $('#ChapterId').append('<option value="'+item.cl_su_st_ch_id+'">'+item.chapter_name+'</option>');
                        });
                    }
                });
            }
        }
    </script>
@endsection
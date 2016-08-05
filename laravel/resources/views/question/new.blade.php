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
                                                    <select name="SubjectId" id="SubjectId"  class="span6"   style="width:250px;" onchange="loadTopics()">
                                                        <option value="">-- Select --</option>
                                                        @if(isset($subjects))
                                                            @foreach($subjects as $subject)
                                                                @if(isset($selected_subject)&& strcmp($subject->cl_su_id , $selected_subject))
                                                                    <option value="{{ $subject->cl_su_id }}" selected = "Selected"> {{$subject->classname ." - ". $subject->subjectname}}</option>
                                                                @else
                                                                    <option value="{{ $subject->cl_su_id }}"> {{$subject->class_name ." - ". $subject->subject_name}}</option>
                                                                @endif
                                                            @endforeach
                                                        @endif
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="control-group" id='displayTopic'>
                                                <label class="control-label">Topics</label>
                                                <div class="controls" id='loadtopic' ></div>
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
                                                    <textarea id="question" name="question"></textarea>
                                                </div>
                                                <div class="control-group">
                                                    <label class="control-label">Question Diagram</label>
                                                    <div class="controls" >
                                                        <input class="" type="file" name="question_diagram" id="question_diagram" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="optionsObj">
                                                <div class="control-group">
                                                    <label class="control-label">Options</label>
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
                                                    <input class="" type="file" name="solution" id="solution" />
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">Level</label>
                                                <div class="controls" >
                                                    <select name="Level" id="Level" style="width:220px;">
                                                        <option value="">-- Select Level of Question --</option>
                                                        <option value="2">Easy</option>
                                                        <option value="5">Medium</option>
                                                        <option value="8">Hard</option>
                                                    </select>
                                                </div>
                                            </div>

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
    <script src="//cdn.ckeditor.com/4.5.7/standard/ckeditor.js"></script>
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

            var options = {
                target:        '#output',   // target element(s) to be updated with server response
                beforeSubmit:  showRequest,  // pre-submit callback
                success:       showResponse  // post-submit callback

                // other available options:
                //url:       url         // override for form's 'action' attribute
                //type:      type        // 'get' or 'post', override for form's 'method' attribute
                //dataType:  null        // 'xml', 'script', or 'json' (expected server response type)
                //clearForm: true        // clear all form fields after successful submit
                //resetForm: true        // reset the form after successful submit

                // $.ajax options can be used here too, for example:
                //timeout:   3000
            };

            // bind form using 'ajaxForm'
            $('#frmquestion').ajaxForm(options);

        });

        function showRequest(formData, jqForm, options) {
            var question = CKEDITOR.instances.question.getData();
            if (question=="") {
                alert("Please insert question");
                return false;
            }

            var opt1 = CKEDITOR.instances.Option1.getData();
            var opt2 = CKEDITOR.instances.Option2.getData();
            var opt3 = CKEDITOR.instances.Option3.getData();
            var opt4 = CKEDITOR.instances.Option4.getData();
            if (opt1=='' || opt2=='' || opt3=='' || opt4=='') {
                alert("Please all all options");
                return false;
            }
            // here we could return false to prevent the form from being submitted;
            // returning anything other than false will allow the form submit to continue
            return true;
        }

        // post-submit callback
        function showResponse(responseText, statusText, xhr, $form)  {
            alert(responseText);
        }

        $("#cancleForm").click(function() {
            window.location = "{{ url('/question/list') }}";
        });

        $('#frmquestion').validate({
            errorClass:'myClass',

            rules: {
                "TeacherId":{ required: true},
                "SubjectId":{ required: true},
                "QuesType":{required: true},
                "question":{required: true},
                "Opt":{required: true},
                "optionsRadioG":{required: true},
                "Level":{required: true},
            },

            messages: {
                "TeacherId":{ required: "Please reload cannot get your id"},
                "SubjectId":{ required: "Please select Class and Subject"},
                "QuesType":{required: "Please select Question Type"},
                "question":{required: "Please insert question"},
                "Opt":{required: "Please insert all options"},
                "optionsRadioG":{required: "Please select the correct answer to this question"},
                "Level":{required: "Please select question level"},
            }
        });

        function loadTopics()
        {
            var subjectId=$("#SubjectId").val();
            if(subjectId!=""){
                var dataToSend="SubjectId="+subjectId;
                $.ajax({
                    url:'{{ url('/api/topic/list') }}',
                    type:'POST',
                    data: dataToSend,
                    success:function(response) {
                        $('#loadtopic').html(response);
                    }
                });
            }
        }
    </script>
@endsection
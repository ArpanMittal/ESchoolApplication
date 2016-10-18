@extends('layouts.app')
@extends('layouts.navigation')
@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <div class="panel panel-default">
                    <div class="panel-heading">Package</div>
                    <div class="panel-body">
                        <div class="widget-body">
                            <div class="container-fluid">
                                <div class="row-fluid">
                                    <div class="span9">
                                        <form class="form-horizontal" action="{{ url('/package/add') }}" method="POST" enctype="multipart/form-data" id="formpackage">
                                            {{ csrf_field() }}
                                            <h5>Create Package</h5>
                                            <hr>
                                            <div class="control-group">
                                                <label class="control-label">Package Name</label>
                                                <div class="controls">
                                                   <input type="text" name="PackageName" class="span9" placeholder="Enter package name" required>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Package Type</label>
                                                <div class="controls">
                                                    <input type="checkbox" name="PackageType" id="packageType" value="1">Exam Package
                                                </div>
                                            </div>
                                            <div class="control-group" id="examdiv">
                                                <label class="control-label">Select Exam</label>
                                                <div class="controls">
                                                    <select name="ExamTag">
                                                        <option value="">-- Select --</option>
                                                        @if(isset($exams))
                                                            @foreach($exams as $exam)
                                                                <option value="{{ $exam->id }}">{{$exam->exam_name}}</option>
                                                            @endforeach
                                                        @endif
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="control-group">
                                                <label class="control-label">Select Contents of Package</label>
                                                <div class="controls">
                                                    <ul class="package-ul">
                                                        @if(isset($subjects))
                                                            @foreach($subjects as $subject)
                                                                 <li>
                                                                     <input type="checkbox" name="subjects[]" value="{{$subject->cl_su_id}}">{{$subject->class_name." - ".$subject->subject_name}}
                                                                     <ul style="list-style: none">
                                                                         @foreach($chapters as $chapter)
                                                                             @if(strpos($chapter->cl_su_st_ch_id,$subject->cl_su_id) !== false)
                                                                                 <li>
                                                                                     <input type="checkbox" name="chapters[]" value="{{$chapter->cl_su_st_ch_id}}">{{$chapter->chapter_name}}
                                                                                     <ul style="list-style: none">
                                                                                         @foreach($topics as $topic)
                                                                                             @if(strpos($topic->hash,$chapter->cl_su_st_ch_id) !== false)
                                                                                                 <li>
                                                                                                    <input type="checkbox" name="topics[]" value="{{$topic->hash}}">{{$topic->topic_name}}
                                                                                                 </li>
                                                                                             @endif
                                                                                         @endforeach
                                                                                     </ul>
                                                                                 </li>
                                                                             @endif
                                                                         @endforeach
                                                                     </ul>
                                                                 </li>
                                                            @endforeach
                                                        @endif
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="form-actions">
                                                <button type="submit" class="btn btn-info" id="epSave" name="epSave">Save</button>
                                                <button type="reset" id="cancleForm" class="btn">Cancel</button>
                                            </div>
                                        </form>
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
    <script>

        $(function () {

            $("#examdiv").hide();
        });

        $("#packageType").click(function () {


            if($(this).prop("checked") == true){

                $("#examdiv").show();
            }

            else if($(this).prop("checked") == false){

                $("#examdiv").hide();
            }
        });

        $('#epSave').click(function () {

            var flag = false;
            if (!$("#formpackage").find("input:checkbox:checked").length > 0) {
               flag = true;
            }
            if (flag) {
                alert ("Nothing selected to add to the package");
                return false;
            }
            return true;
        });

        $('input[type="checkbox"]').change(function(e) {

            var checked = $(this).prop("checked"),
                    container = $(this).parent(),
                    siblings = container.siblings();

            container.find('input[type="checkbox"]').prop({
                indeterminate: false,
                checked: checked
            });

            function checkSiblings(el) {

                var parent = el.parent().parent(),
                        all = true;

                el.siblings().each(function() {
                    return all = ($(this).children('input[type="checkbox"]').prop("checked") === checked);
                });

                if (all && checked) {

                    parent.children('input[type="checkbox"]').prop({
                        indeterminate: false,
                        checked: checked
                    });

                    checkSiblings(parent);

                } else if (all && !checked) {

                    parent.children('input[type="checkbox"]').prop("checked", checked);
                    parent.children('input[type="checkbox"]').prop("indeterminate", (parent.find('input[type="checkbox"]:checked').length > 0));
                    checkSiblings(parent);

                } else {

                    el.parents("li").children('input[type="checkbox"]').prop({
                        indeterminate: true,
                        checked: false
                    });
                }
            }
            checkSiblings(container);
        });
    </script>
@endsection
@extends('layouts.app')
@extends('layouts.navigation')
@section('script_header')
    <style >
    .sort{
        background: url("{!! asset('img/sorting_asc.png') !!}") no-repeat scroll right center transparent !important;
        cursor: pointer;
        }
    </style>
    <script language="javascript" type="text/javascript">
        var recordName='Questions';
        function paging(i) {
            document.thisForm.iPageNum.value=i;
            document.thisForm.submit();
        }
    </script>
@endsection
@section('content')
<div class="container">
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
            <div class="panel panel-default">
                <div class="panel-heading">Question<a href="{{ url('/question') }}" > <button  type="button" style="float:right">Add</button></a></div>
                <div class="panel-body">
                    <div class="widget-body search">
                        <form class="form-horizontal" name="questionlistform" action="{{ url('/question/search') }}" method="POST" enctype="multipart/form-data" id="frmqueslist">
                            {{ csrf_field() }}
                            <table style="margin-top:20px">
                                <tr>
                                    <td>
                                        <div class="control-group">
                                            <label class="control-label">Question</label>
                                            <div class="controls">
                                                <input name="Question" id="Question" class="span9 input-left-top-margins" type="text" placeholder="Question" style="width: 65%" value=""  >
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="control-group">
                                            <label class="control-label">Subject</label>
                                            <div class="controls">
                                                <select name="SubjectId" id="SubjectId" >
                                                    <option value="">-- Select Subject --</option>
                                                    @if(isset($subjects))
                                                        @foreach($subjects as $subject)
                                                                <option value="{{ $subject->cl_su_id }}"> {{$subject->class_name ." - ". $subject->subject_name}}</option>
                                                        @endforeach
                                                    @endif
                                                </select>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="control-group">
                                            <label class="control-label">Topic</label>
                                            <div class="controls">
                                                <select name="TopicId" id="TopicId" >
                                                    <option value="">-- Select Topic --</option>
                                                    @if(isset($topics))
                                                        @foreach($topics as $topic)
                                                            <option value="{{ $topic->hash }}"> {{$topic->chapter_name ." - ". $topic->topic_name}}</option>
                                                        @endforeach
                                                    @endif
                                                </select>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="control-group">
                                            <label class="control-label">Tag</label>
                                            <div class="controls">
                                                <select name="TagId" id="TagId" >
                                                    <option value="">-- Select Tag --</option>
                                                    @if(isset($tags))
                                                        @foreach($tags as $tag)
                                                            <option value="{{ $tag->id }}"> {{$tag->exam_name}}</option>
                                                        @endforeach
                                                    @endif
                                                </select>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr colspan="3" style="float:right	">
                                    <td align="right" style="padding-right:85px">
                                        <input name="Search" type="submit" id="Search" class="btn btn-info" value="Search">
                                        <input name="Search" type="reset" id="resetform" class="btn btn-info" value="Clear">
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="widget-body">
                        <div id="dt_example" class="example_alt_pagination">
                            <table class="table table-condensed table-striped table-hover table-bordered pull-left" id="data-table">
                                <thead>
                                <tr  class="gradeA success">
                                    <th  style="text-align:center" class ="sort">S.No</th>
                                    <th style="text-align:center">Class</th>
                                    <th style="text-align:center">Subject - Stream - Chapter - Topic</th>
                                    <th  style="text-align:center">Question</th>
                                    <th  style="text-align:center">Question Type</th>
                                    <th  style="text-align:center;width:70px" class ="sort">Action</th>
                                </tr>
                                </thead>
                                <tbody>


                                @if(isset($question_list))
                                    @foreach($question_list as $question)
                                    <tr class="gradeA info">
                                        <td style="text-align:right;width:60px">{{isset($Sno)?++$Sno:$Sno=1}}</td>
                                        <td style="width:60px">{{$question->class_name}}</td>
                                        <td>{{$question->subject_name." - ". $question->stream_name." - ".$question->chapter_name ." - ". $question->topic_name}}</td>
                                        <td style="text-align:left">{{$question->question}}</td>
                                        <td style="text-align:left">{{$question->question_type}}</td>
                                        <td style="text-align:center">
                                            <a href="{{ url('/question/'.$question->id) }}" title='Edit'  data-icon="&#xe005"  data-original-title="Edit">
                                                <span class="fs1" aria-hidden="true"></span>
                                            </a>
                                            <a href="{{ url('/question/view/'.$question->id) }}" title='Preview'  data-icon="&#xe0c6"  data-original-title="Preview">
                                                <span class="fs1" aria-hidden="true"></span>
                                            </a>
                                        </td>
                                    </tr>
                                    @endforeach
                                @endif
                                </tbody>
                            </table>
                            <div class="clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection
@section('script')
    <script src="{!! asset('js/jquery.dataTables.js') !!}"></script>
    <script>
        $(document).ready(function () {
                $('#data-table').dataTable({
                "sPaginationType": "full_numbers"
            });
            $("#SubjectId").val('{{$selected_subject}}');
            $("#TopicId").val('{{$selected_topic}}');
            $("#TagId").val('{{$selected_tag}}');
            @if(isset($selected_question))
            $("#Question").val('{{$selected_question}}');
            @endif
        });
    </script>
@endsection
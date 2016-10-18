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
                                        <h5>Question Preview</h5>
                                        <hr>
                                        <div class="control-group">
                                            <div class="controls">
                                                @if(isset($subject))
                                                    @if(isset($subject))
                                                        @if(isset($chapter))
                                                            @if(isset($topic))
                                                                <label>Path</label>
                                                                <h3 style="width: 600px">
                                                                    {{$subject->subject_name." - ".$chapter->chapter_name." - ".$topic->topic_name}}
                                                                </h3>
                                                                <label>Stream</label>
                                                                <h3 class="pull-right">
                                                                    {{$stream->stream_name}}
                                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                    <a href="{{ url('/question/'.$question->id) }}" title='Edit'  data-icon="&#xe005"  data-original-title="Edit">
                                                                        <span class="fs1" aria-hidden="true"></span>
                                                                    </a>
                                                                </h3>
                                                            @endif
                                                        @endif
                                                    @endif
                                                @endif
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <div class="controls">
                                                @if(isset($question))
                                                    @foreach($tags as $tag)
                                                        <span class="tags">{{$tag->exam_name}}</span>
                                                    @endforeach
                                                @endif
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <div class="controls">
                                                @if(isset($questype))
                                                    <h4>{{$questype->question_type}}</h4>
                                                @endif
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <div>
                                                @if(isset($question))
                                                    <h4 class="controls"><strong>Q:&nbsp;</strong>{!! $question->question !!}</h4>
                                                    @if(isset($question->image_path) && $question->image_path != "")
                                                        <img src="{!! asset($question->image_path) !!}">
                                                    @endif
                                                    <ol type="a">
                                                        @if(isset($options))
                                                            @foreach($options as $option)
                                                                @if(isset($answer))
                                                                    @if($option->id == $answer[0]->answer)
                                                                        <li style="background-color: rgba(0,255, 0, 0.5)">
                                                                            {!! $option->opt !!}
                                                                        </li>
                                                                    @else
                                                                        <li>
                                                                            {!! $option->opt !!}
                                                                        </li>
                                                                    @endif
                                                                @endif
                                                            @endforeach
                                                        @endif
                                                    </ol>
                                                @endif
                                            </div>
                                        </div>

                                        <div class="control-group">
                                            <div class="controls" >
                                                @if(isset($question->solution_path) && $question->solution_path != "")
                                                    <h4>Solution</h4>
                                                    <img src="{!! asset($question->solution_path) !!}">
                                                @endif
                                            </div>
                                        </div>

                                        <div class="control-group">
                                            <div class="controls">
                                                <h4>Ideal Time: {{$question->ideal_attempt_time}}</h4>
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <div class="controls" >
                                                <h4>Difficulty: {{$question->difficulty}}/10</h4>
                                            </div>
                                        </div>
                                        @if(isset($question))
                                            <button type="button" class="btn btn-info" id="nextButton" name="nextButton" >Next</button>
                                        @endif
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
    <script src="http://malsup.github.com/jquery.form.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    <script type="text/javascript">
        $("#nextButton").click(function() {
            window.location = '{{ url('/question/view/next?QuestionId='.$question->id) }}';
        });
    </script>
@endsection
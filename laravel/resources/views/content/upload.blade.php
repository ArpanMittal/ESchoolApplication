@extends('layouts.app')
@extends('layouts.navigation')
@section('script_header')
    <style type="text/css">
        .myClass
        {
            color:red;
        }
        .progressLabel {
            text-align: center; /* If you want to center it */
            line-height: 30px; /* Set the line-height to the same as the height of the progress bar container, to center it vertically */
            color: white;
            background-color: transparent;
        }
        .myProgress {
            position: static;
            display: inline-block;
            width: 25%;
            height: 30px;
            background-color: grey;
        }
        .myBar {
            position: absolute;
            width: 1%;
            height: 30px;
            background-color: green;
        }
        .myBarError {
            position: absolute;
            width: 1%;
            height: 30px;
            background-color: red;
        }

    </style>
@endsection
@section('content')
<div class="container">
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
            <div class="panel panel-default">
                <div class="panel-heading">Content </div>
                <div class="panel-body">
                    <div class="widget-body">
                        <div class="container-fluid">
                            <div class="row-fluid">
                                <div class="span12">
                                    <form class="form-horizontal" action="{{ url('/content/add/'.isset($selected->cl_su_st_ch_id)) }}" method="POST" enctype="multipart/form-data" id="frmcontent" name="frmcontent"  >
                                        {{ csrf_field() }}
                                        <br />
                                        <h5>Content  Information</h5>
                                        <hr>
                                        <div class="control-group">
                                            <label class="control-label">
                                                Class, Subject , Stream & Chapter
                                            </label>
                                            <div class="controls">
                                                <label>Class {!! isset($selected)?$selected->class_name." - ".$selected->subject_name." - ".$selected->stream_name." - ".$selected->chapter_name:"NULL" !!}</label>
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <label class="control-label">List of content</label>
                                            <div class="controls">
                                                <table class="table table-condensed table-striped table-hover table-bordered pull-left" id="data-table">
                                                    <thead>
                                                    <tr class="gradeA success">
                                                        <th style="text-align:center;" class ="sort">Sl. No</th>
                                                        <th style="text-align:center">Topic</th>
                                                        <th style="text-align:center;" class ="sort">Document Action</th>
                                                        <th style="text-align:center;" class ="sort">Video Action</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    @foreach($contents as $content)
                                                    <tr class="gradeA info content">
                                                        <td>{{isset($Sno)?++$Sno:$Sno=1}}</td>
                                                        <td>{{$content->topic_name}}</td>
                                                        <td class="hidden-phone">
                                                            <input type="file" name="{!!$content->hash!!}_pdf">

                                                        </td>
                                                    </tr>

                                                    @endforeach

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="form-actions">
                                            <button type="submit" class="btn btn-info" id="epSave" name="epSave">Save</button>
                                            <button type="reset" id="resetform" class="btn">Cancel</button>
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
    <script src="{!! asset('js/jquery.dataTables.js') !!}"></script>
    <script>
        $(document).ready(function () {
                $('#data-table').dataTable({
                "sPaginationType": "full_numbers"
            });
        });
    </script>
@endsection
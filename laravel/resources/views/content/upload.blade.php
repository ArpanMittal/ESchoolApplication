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
                                    <form class="form-horizontal" action="{{ url('/content/add/'.(isset($selected->cl_su_st_ch_id)?$selected->cl_su_st_ch_id:"NULL")) }}" method="POST" enctype="multipart/form-data" id="frmcontent" name="frmcontent"  >
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
                                                            <input type="file" class="FileSelect" id="{!!$content->hash!!}_pdf" />
                                                            @if(isset($content->pdf_path))
                                                                <a target="_blank" href='{{ url($content->pdf_path) }}' class="icon-eye"  title='View PDF' ></a>
                                                            @endif
                                                        </td>
                                                        <td class="hidden-phone">
                                                            <input type="file" class="FileSelect" id="{!!$content->hash!!}_vid" />
                                                            @if(isset($content->video_path))
                                                                <a target="_blank" href='{{ url($content->video_path) }}' class="icon-eye"  title='View Video' ></a>
                                                            @endif
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
    <div id="output"></div>
@endsection
@section('script')
    <script src="{!! asset('js/jquery.dataTables.js') !!}"></script>
    <script>
        $(document).ready(function () {
                $('#data-table').dataTable({
                "sPaginationType": "full_numbers"
            });
        });
        $(".FileSelect").change(function () {
            // get the file name, possibly with path (depends on browser)
            var filename = $(this).val();

            // Use a regular expression to trim everything before final dot
            var extension = filename.replace(/^.*\./, '');

            // Iff there is no dot anywhere in filename, we would have extension == filename,
            // so we account for this possibility now
            if (extension == filename) {
                extension = '';
            } else {
                // if there is an extension, we convert to lower case
                // (N.B. this conversion will not effect the value of the extension
                // on the file upload.)
                extension = extension.toLowerCase();
            }
            type = this.id.split("_")[1];
            if(type == 'pdf' && extension != 'pdf'){
                alert("only pdf files allowed");
                this.value = "";
                return false;
            }else if(type == 'vid' && extension != 'mp4'){
                alert('Only mp4 files allowed');
                this.value="";
                return false;
            }else{
                $( this ).parent().append('<div class="myProgress"><input type="hidden" name="'+this.id+'" value="'+this.id.split("_")[0]+'.'+extension+'"/> <div class="myBar" id="progress__'+this.id+'"><div class="progressLabel__'+this.id+'" id="progressLabel__'+this.id+'">0%</div></div></div><button type="button" onclick="handleFileUpload(this.id)" id="upload__'+this.id+'" >upload</button>');
            }
        });
        function handleFileUpload(id) {
            var i = id.split("__")[1];
            $("#progress__"+i).addClass("myBar");
            $("#progress__"+i).removeClass("myBarError");
            if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
                alert('The File APIs are not fully supported in this browser.');
                return;
            }

            input = document.getElementById(i);
            if (!input) {
                alert("Um, couldn't find the file element.");
            }
            else if (!input.files) {
                alert("This browser doesn't seem to support the `files` property of file inputs.");
            }
            else {
                var file = input.files[0];
                readBlob(file,0,i);
            }
        }
        function readBlob(file,start,i) {
            var temp = start + 512000;
            var stop = file.size - 1;
            if(temp>stop){
                temp = stop +1;
            }
            if(start < stop){
                var reader = new FileReader();

                // If we use onloadend, we need to check the readyState.
                reader.onloadend = function(evt) {
                    if (evt.target.readyState == FileReader.DONE) { // DONE == 2
                        var data = evt.target.result;
                        var percent = Math.floor((temp/stop)* 25);
                        $("#progress__"+i).width( percent  + "%" );
                        $("#progressLabel__"+i).text((percent*4)+"%");
                        var filename = i.split("_")[0]+"."+file.name.split(".")[1];
                        $.post( "{{ url('/content/receiver') }}", { 'fname': filename, 'start': start, 'data': data })
                                .done(function( rec ) {
                                    if(rec == start){
                                        readBlob(file,temp,i);
                                    }else{
                                        $("#progressLabel__"+i).text(rec);
                                        $("#progress__"+i).addClass("myBarError");
                                        $("#progress__"+i).removeClass("myBar");
                                    }
                                })
                                .error(function( rec ) {
                                    $("#output").append(rec.responseText);
                                    $("#progressLabel__"+i).text("error");
                                    $("#progress__"+i).addClass("myBarError");
                                    $("#progress__"+i).removeClass("myBar");
                                });
                    }
                };

                var blob = file.slice(start, temp);
                reader.readAsDataURL(blob);
            }else {
                $("#progressLabel__"+i).text("completed");
            }
        }
    </script>
@endsection
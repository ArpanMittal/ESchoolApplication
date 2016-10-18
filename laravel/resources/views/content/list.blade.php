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
                <div class="panel-heading">Content</div>
                <div class="panel-body">
                    <div class="widget-body">
                        <div id="dt_example" class="example_alt_pagination">
                            <table class="table table-condensed table-striped table-hover table-bordered pull-left" id="data-table">
                                <thead>
                                <tr  class="gradeA success">
                                    <th  style="text-align:center" class ="sort">S.No</th>
                                    <th style="text-align:center">Class</th>
                                    <th style="text-align:center">Summery</th>
                                    <th  style="text-align:center;width:70px" class ="sort">Action</th>
                                </tr>
                                </thead>
                                <tbody>


                                @if(isset($content_class_list))
                                    @foreach($content_class_list as $class)
                                    <tr class="gradeA info">
                                        <td style="text-align:right;width:60px">{{isset($Sno)?++$Sno:$Sno=1}}</td>
                                        <td style="width:60px">{{$class->name}}</td>
                                        <td style="text-align:left">{{$class->summery}}</td>
                                        <td style="text-align:center">
                                            <a href="{{ url('/content/list/subject/'.$class->id) }}" title='Preview'  data-icon="&#xe02f;"  data-original-title="Preview">
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
        });
    </script>
@endsection
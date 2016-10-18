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
        var recordName='Packages';
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
                    <div class="panel-heading">Package<a href="{{ url('/package/create') }}" > <button  type="button" style="float:right">Add</button></a></div>
                    <div class="panel-body">
                        <div class="widget-body">
                            <div id="dt_example" class="example_alt_pagination">
                                <table class="table table-condensed table-striped table-hover table-bordered pull-left" id="data-table">
                                    <thead>
                                    <tr  class="gradeA success">
                                        <th  style="text-align:center" class ="sort">S.No</th>
                                        <th style="text-align:center">Package Name</th>
                                        {{--<th style="text-align:center">Content</th>--}}
                                    </tr>
                                    </thead>
                                    <tbody>

                                    @if(isset($packages))
                                        @foreach($packages as $package)
                                            <tr class="gradeA info">
                                                <td style="text-align:right;width:60px">{{isset($Sno)?++$Sno:$Sno=1}}</td>
                                                <td style="width: 200px">{{$package->package_name}}</td>
                                                {{--<td style="text-align:left">--}}
                                                {{--@foreach($submap as $submaps)--}}
                                                    {{--@if($submaps->pack_id == $package->id)--}}
                                                            {{--{{$submaps->item_id.", "}}--}}
                                                    {{--@endif--}}
                                                {{--@endforeach--}}
                                                {{--</td>--}}
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
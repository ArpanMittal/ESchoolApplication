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
                    <div class="panel-heading">Result</div>
                    <div class="panel-body">
                        <div class="widget-body search">
                            @if (count($errors) > 0)
                                <div class="alert alert-danger">
                                    <ul>
                                        @foreach ($errors->all() as $error)
                                            <li>{{ $error }}</li>
                                        @endforeach
                                    </ul>
                                </div>
                            @endif
                            <?php $rules = ["school" => "required", "date" => "required|date"]; ?>
                            {!! Form::open(array('url' => 'result', 'class' => 'form', 'method' => 'post'),$rules) !!}
                            {!! Form::token() !!}
                            <div class="form-group">
                                {!! Form::label('Date') !!}
                                {!! Form::input('date', 'date', \Carbon\Carbon::now(),
                                    array('required',
                                          'class'=>'form-control')) !!}
                            </div>

                            <div class="form-group">
                                {!! Form::label('School') !!}
                                @if(isset($schools))
                                {!! Form::select('school',$schools,null,
                                    array('required',
                                          'class'=>'form-control',
                                          'placeholder'=>'School Name')) !!}
                                @endif
                            </div>



                            <div class="form-group">
                                {!! Form::submit('Get',
                                  array('class'=>'btn btn-primary')) !!}
                            </div>
                                {!! Form::close() !!}
                        </div>
                        <div class="widget-body">
                            <div id="dt_example" class="example_alt_pagination">
                                <table class="table table-condensed table-striped table-hover table-bordered pull-left" id="data-table">
                                    <thead>
                                    <tr  class="gradeA success">
                                        <th  style="text-align:center" class ="sort">S.No</th>
                                        <th style="text-align:center">Name</th>
                                        <th style="text-align:center">Email</th>
                                        <th  style="text-align:center">Corrent</th>
                                        <th  style="text-align:center">Incorrent</th>
                                        <th  style="text-align:center">Total</th>
                                    </tr>
                                    </thead>
                                    <tbody>


                                    @if(isset($results))
                                        @foreach($results as $result)
                                            <tr class="gradeA info">
                                                <td style="text-align:right;width:60px">{{isset($Sno)?++$Sno:$Sno=1}}</td>
                                                <td style="text-align:left">{{$result->name}}</td>
                                                <td style="text-align:left">{{$result->email}}</td>
                                                <td style="text-align:right">{{$result->correct}}</td>
                                                <td style="text-align:right">{{$result->incorrect}}</td>
                                                <td style="text-align:right">{{$result->total}}</td>
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
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    <script src="{{ asset('laravalid/jquery.validate.laravalid.js') }}"></script>
    <script type="text/javascript">
        $('form').validate({onkeyup: false});
    </script>
    <script>
        $(document).ready(function () {
            $('#data-table').dataTable({
                "sPaginationType": "full_numbers"
            });
            @if(isset($selected_date))
                $('input[name="date"]').val("{{$selected_date}}");
            @endif
            @if(isset($selected_school))
                $('select[name="school"]').val("{{$selected_school}}");
            @endif
        });
    </script>
@endsection
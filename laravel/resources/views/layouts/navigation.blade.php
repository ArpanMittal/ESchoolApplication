@section('navigation')
    @if(isset($user))
        @if ($user->role_id==1 || $user->role_id==2 || $user->role_id==3)
            <li><a href="{{ url('/question/list') }}">Question</a></li>
            <li><a href="{{ url('/content/list/class') }}">Content</a></li>
        @endif
        @if($user->role_id==1)
            <li><a href="{{ url('/package/list') }}">Package</a></li>
            <li><a href="{{ url('/result') }}">Result</a></li>
        @endif
    @endif
@endsection
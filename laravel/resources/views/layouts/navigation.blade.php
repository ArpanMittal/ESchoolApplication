@section('navigation')
    @if (isset($user) || $user->role_id==1 || $user->role_id==2 || $user->role_id==3)
        <li><a href="{{ url('/question/list') }}">Question</a></li>
        <li><a href="{{ url('/content/list/class') }}">Content</a></li>
    @endif
@endsection
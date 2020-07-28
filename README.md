# Tobe1JBG

그룹 단위로 제한된 기간동안 목표를 설정하고 달성률을 기록합니다. 서버 정보를 이용하면 랭킹을 공유하는 인텐트도 만들 수 있습니다.
크게 Facebook Activity에서 로그인 작업을 완료한 것을 확인하고 Facebook ID와 닉네임을 넘겨주고, splash activity를 실행한 후, main activity로 이동합니다.

Facebook Activity에서는 기존에 로그인을 했다면 AccessToken으로부터 id를 얻고, 이를 서버에 전달하여 닉네임을 받아 splash activity로 보냅니다.
로그인이 되어있지 않다면 로그인을 할 수 있도록 facebook intent를 실행하고, 서버에 등록되지 않은 id인지 서버에서 확인한 후 미가입자면 RegisterDialog를 띄워 닉네임과 이름을 작성하는간단한 회원가입 절차를 진행합니다.

MainActivity는 home Fragment, grouplist fragment, my page fragment 세개의 프래그먼트를 가지며, bottom_navigation을 통해 이동합니다.

1) HomeFragment

하루 단위로 입력이 이루어집니다. 간단한 그룹 정보를 보여주며, 타입에 따라 입력 방식이 다릅니다.
Group 정보는 HomeViewModel의 init() 메서드를 통해 MyGroupInfo 객체에 저장하는데, 
type = 0인 경우 주변 와이파이와 기준 와이파이를 비교하여 해당 장소에 머물렀던 시간을 자동으로 체크하게 만들려고 했고(구현 못함)
type = 1인 경우 직접 입력하여 일일 성과를 저장하도록 했습니다. 각각의 recyclerview layout은 0일때 home_type_time, 1일때는 home_type_number입니다.

2) GroupList fragment

그룹 단위의 정보를 보여줍니다.

하단의 floating action button을 누르면 NewGroupActivity로 이동합니다. 
여기서는 주변 Wifi 정보를 검색받아 WifiData에 저장하고 WifiAdapter class를 이용해 리스트뷰에 저장하며, server에서 유저 정보를 요청해 선택할 수 있도록 합니다. 
nullcheck() 메소드를 통해 비어있는 박스가 있다면 "모든 정보를 입력해주세요."라고 적힌 토스트 메세지를 띄우고, 아니라면 서버에 그룹 추가를 요청합니다.

3) MyPage fragment

개인정보 수정 및 친구 초대 기능을 추가하려고 만든 창입니다. 지금은 그냥 랜덤한 인터넷 주소를 띄웁니다.



tmon-clip
=========

Tmon Clip Share

기능
  1. 일정 간격으로 클라이언트 정보를 IpMapServer에 등록 (하트비트)
    A. [RelayService]
      - [ClientInfo] : ID, Password, IP, Port
      - IP, Port : IpMapServer정보
      - startHearBeatService() : 주기적으로(1분?) 하트비트를 보낼 수 있는 쓰레드 생성
        > [HearBeatService]
      - startEventReceiver() : 클립보드 갱신 이벤트를 수신할 수 있는 서버 쓰레드 생성
        > [EventReceiver]
          >> [RemoteClipListener] 등록
  2. 시스템 클립보드 변경 이벤트 잡기 (LocalClipListener)
  3. 클립 보드의 내용을 ClipDataServer(sftp)에 저장
  4. 클립 보드 저장 완료 이벤트를 IpMapServer에 전송
  5. 클립 보드 저장 완료 이벤트를 받음
  6. ClipDataServer(sftp)에서 클립보드 받기

ClipboardService (implements RemoteClipListener)
- 시스템 클립보드 입출력
- ClipDataDao를 이용하여 서버에 입출력
 
interface RemoteClipListener
- onRemoteChanged()
- onLocalChanged()

ClipDataDao
- ClipDataServer 입출력
 
EventReceiver (Observer)
- 이벤트 수신기
- RemoteClipListener : onRemoteChanged
 
EventSender
- 이벤트 발신
- RemoteClipListener : onLocalChanged


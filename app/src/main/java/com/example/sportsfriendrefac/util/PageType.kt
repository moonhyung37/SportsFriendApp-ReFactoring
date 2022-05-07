package com.example.sportsfriendrefac.util


/*바텀 네비게이션 메뉴를 관리하는 클래스 */
enum class PageType(val title: String, var tag: String) {
    PAGE1("BulletinFrag", "tag_BulletinFrag"),
    PAGE2("FriendListFrag", "tag_FriendListFrag"),
    PAGE3("ChatRoomFrag", "tag_ChatRoomFrag"),
    PAGE4("myPageFragment", "tag_myPageFrag");

}
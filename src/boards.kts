#
# Version: $Id: boards.kts,v 1.3 2001/01/16 12:54:29 philippe Exp $
# License: Gnu GPL (see file LICENSE)
#
#  This program is free software; you can redistribute it and/or modify 
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#

The map name is inside <>
The map description is inside @@

Possible pieces are :
* : main piece
[a-zA-Z0-9] : other piece (can be used multiple times)
# : wall
- : wall that only the main piece can go through
. : destination of the main piece



<Splash>
@--  --                       @
@-a zz-                       @
@-a*z   c ... eee eee a cc aaa@
@-**    c . .  d  e   abc   b @
@-b*i   c . .  d   e  bb    b @
@-b ii- c . .  d    e abc   b @
@--  -- c ...  d  eee a cc aaa@

<Easy>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #m**n#  @
@  #cdef#  @
@  #ghij#  @
@  #k  l#  @
@  ##--##  @
@        ..@
@        ..@   


<Daisy>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #a**b#  @
@  #cdef#  @
@  #ghij#  @
@  #k  l#  @
@  ##--##  @
@        ..@
@        ..@   

<Violet>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #a**b#  @
@  #cdef#  @
@  #cghi#  @
@  #j  k#  @
@  ##--##  @
@        ..@
@        ..@

<Poppy>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #a**b#  @
@  #cdde#  @
@  #fghi#  @
@  #j  k#  @
@  ##--##  @
@        ..@
@        ..@

<Pansy>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #a**b#  @
@  #cdef#  @
@  #cghf#  @
@  #i  j#  @
@  ##--##  @
@        ..@
@        ..@

<Snowdrop>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #a**b#  @
@  #cdde#  @
@  #cfgh#  @
@  #i  j#  @
@  ##--##  @
@        ..@
@        ..@

<Forget me not (ane rouge)>
@          @
@          @
@  ######  @
@  #a**b#  @
@  #a**b#  @
@  #cdde#  @
@  #cfge#  @
@  #h  i#  @
@  ##--##  @
@        ..@
@        ..@

<Trail>
@  ######  @
@  #a**c#  @
@  #a**c#  @
@  #eddg#  @
@  #hffj#  @
@  # ii #  @
@  ##--##  @
@      ..  @
@      ..  @

<Ambush>
@  ######  @
@  #a**c#  @
@  #d**e#  @
@  #dffe#  @
@  #ghhi#  @
@  # jj #  @
@  ##--##  @
@      ..  @
@      ..  @


<Dad's puzzle>
@  ######  @
@  #**bb#  @
@  #**cc#  @
@  #de  #  @
@  #fghh#  @
@  #fgii#  @
@  #--###  @
@      ..  @
@      ..  @

<Success>
@          @
@ #######  @
@ #**bbc#  @
@ #defgh#  @
@ #ijkgh-  @
@ #llk  #  @
@ #######  @
@          @
@  ..      @ 

<Bone>
@          @
@  ######  @
@  #abc*#  @
@  # dd*#  @
@  # ee*#  @
@  # fgh#  @
@  ##-###  @
@        . @
@        . @
@        . @

<Fortune>
@        ..@
@        . @
@ ####-- . @
@ #ab  -   @
@ #ccd #   @
@ #ccd #   @
@ #**ee#   @
@ #*fgh#   @
@ #*iih#   @
@ ######   @
@          @

<Fool>
@  ########@
@  -aabc  #@
@  #aabdef#@
@  #ijggef#@
@  #klhh**#@
@  ########@
@        ..@ 

<Solomon>
@ .        @
@..        @
@  #--#### @
@  #  aab# @
@  # cdfb# @
@  #hcefg# @
@  #hijk*# @
@  #hll**# @
@  ####### @

<Cleopatra>
@  ######  @
@  #abcd#  @
@  #**ee#  @
@  #f*g #  @
@  #fh i-  @
@  ####--  @
@        ..@
@         .@

<Rome>
@ ######## @
@ #abcc**# @
@ #ddeef*# @
@ #ddghfi# @
@ #   jki# @
@ #--##### @
@       .. @
@        . @

<Hanoi>
@ ######     ###### @
@ #****##   ##    # @
@ #bbbbb## ##     # @
@ #cccccc###      # @
@ #ddddddd#       # @
@ #eeeeeeee       # @
@ ########        # @
@        ##       # @
@         ##      # @
@          ##     # @
@           ##    # @
@            #----# @
@             ....  @

<Escape>
@  ###      @
@  #*###### @
@  #abccc # @
@  #adeef## @
@  #adghi#  @
@ ##jkkhi#  @
@ # mmmni#  @
@ ###### #  @
@      #-#  @
@         . @


<Ithaca>
@.aaaaaaaaaaaaaaaaab@
@..  cddeffffffffffb@
@ .. cddeffffffffffb@
@  . cddeffffffffffb@
@ggg-############hhb@
@ggg-  ABCDEFFGH#hhb@
@ggg-       FFIJ#hhb@
@ggg#       KLMJ#hhb@
@ggg#NNNNOOOPQMJ#hhb@
@ggg#NNNNOOOP*RS#hhb@
@ggg#TTTTTUVW**X#hhb@
@ggg#YZ12222W3**#hhb@
@ggg#YZ12222W34*#iib@
@jjj#YZ155555367#klb@
@jjj#############mmb@
@jjjnooooooooooppppb@
@jjjqooooooooooppppb@
@       rrrssssppppb@
@ttttttuvvvvvvvwwwwx@

<Pelopones>
@ ######### @
@ #abbb***# @
@ #abbb*c*# @
@ #adeefgg# @
@ #  eefhh# @
@ #    ihh# @
@ #    ihh# @
@ #---##### @
@       ... @
@       . . @

<Shark>
@...           @
@ .            @
@   ########   @
@   #abijjk-   @
@   #ccijjl-   @
@   #ddhmnp#   @
@w  #***mop#   @
@w  #e*gq s#   @
@w  #eff r #   @
@w  ########   @
@w             @
@w             @

<Doggie>
@##########    @
@#a**jkkko#    @
@#bb*jlmnp#    @
@#c****qrs#    @
@#def**tuv#    @
@#ghi     -    @
@#######---    @
@          ..  @
@           .  @
@          ....@
@            ..@

<Lodzianka>
@ABBBBBBBBBBBB@
@AC          D@
@AC######### D@
@ C#**abbcc# D@
@ C#**abbdd# D@
@EC#eefgh  # D@
@EC#iiijk  -FZ@
@E #iiijk  -IG@
@E #########IG@
@EHHHH     ..G@
@EHHHHJJJJJ..G@

<Polonaise>
@ ABCCCCCCCC@
@ ABCCCCCCCC@
@ A####### D@
@EE#aab**# D@
@EE#aabc*# D@
@EE#defgg# D@
@  -  fhh# D@
@  #  ihh# D@
@  #--#### D@
@..FF      D@
@ .FFGGGGGGD@

<Baltic sea>
@.       @
@.       @
@ #-#### @
@ # abc# @
@ # dec# @
@ #fggc# @
@ #fhhi# @
@ #fjk*# @
@ #flk*# @
@ ###### @

<American Pie>
@ ########## @
@ #a*bcdefg# @
@ #**bhhhhg# @
@ #*iijjkkg# @
@ #liimnoop# @
@ #qiirrr  # @
@ #qstuvv  # @
@ #qwwxvv  # @
@ ######--## @
@           .@
@          ..@
@          . @

<Transeuropea>
@a     vvvvv   wwwww@
@a     vvvvv   wwwww@
@a   -##-#######    @
@    -iiiiijjkk#    @
@   d-   lopqru#    @
@    #   lopqsu#    @
@  c #   mopqtu#y   @
@  c #   mopq*u#    @
@    #   nn****#xxxx@
@bbb ###########xxxx@
@bbb   fff  ggg   . @
@ eeeeefff      ....@


<SunShine>
@aabzzccccc  ...  ggggghhijj@
@aabzzccccc .. .. ggggghhijj@
@kk llccccc .   . gggggoo pp@
@qqrssccccc .. .. gggggwwxyy@
@qqrssccccc  ...  gggggwwxyy@
@AAAAA#################BBBBB@
@AAAAA#aabccd***effghh#BBBBB@
@AAAAA#aabcc** **ffghh#BBBBB@
@AAAAA#iijkk*   *mmnoo#BBBBB@
@AAAAA#ppqrr** **sstuu#BBBBB@
@     #ppqrrv***wsstuu#     @
@SSTEE#xxxxxyyzAABBBBB#FFGHH@
@SSTEE#xxxxxyyzAABBBBB#FFGHH@
@II JJ#xxxxxCC UUBBBBB#KK LL@
@MMNOO#xxxxxDDEFFBBBBB#PPQRR@
@MMNOO#xxxxxDDEFFBBBBB#PPQRR@
@     #GGHIIJJKLLmmnCC#     @
@AAAAA#GGPIIJJ LLmmQCC#BBBBB@
@AAAAA#RSTUVW X YZ0123#BBBBB@
@AAAAA#4456677 8899abb#BBBBB@
@AAAAA#44c6677d8899ebb#BBBBB@
@AAAAA######-----######BBBBB@
@aabzzccccc ddeff ggggghhijj@
@aabzzccccc ddeff ggggghhijj@
@kk llccccc mm nn gggggoo pp@
@qqrssccccc ttuvv gggggwwxyy@
@qqrssccccc ttuvv gggggwwxyy@




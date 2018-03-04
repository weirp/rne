(ns rne.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [rne.handlers]
              [rne.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))
(def input (r/adapt-react-class (.-TextInput ReactNative)))
(def activity-indicator (r/adapt-react-class (.-ActivityIndicator ReactNative)))
(def button (r/adapt-react-class (.-Button ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def scroll-view (r/adapt-react-class (.-ScrollView ReactNative)))
(def status-bar (r/adapt-react-class (.-StatusBar ReactNative)))

(def ToolbarAndroid (js/require "ToolbarAndroid"))
(def toolbar (r/adapt-react-class (.-ToolbarAndroid ReactNative)))

;;(def MaterialDesign (js/require "react-native-material-design"))

(set! js/React (js/require "react-native"))
;;(set! js/MaterialDesign (js/require "react-native-material-design"))
(set! js/AndroidDialog (js/require "react-native-dialogs"))

(def FontAwesome (js/require "@expo/vector-icons/FontAwesome"))

;;(def DrawerLayoutAndroid (js/require "DrawerLayoutAndroid"))
;;(def drawerLayout (r/adapt-react-class (.-DrawerLayoutAndroid ReactNative)))
;;(def drawer (r/adapt-react-class (.-Drawer ReactNative)))

(def react-navigation (js/require "react-navigation"))
(def add-navigation-helpers (.-addNavigationHelpers react-navigation))
(def stack-navigator (.-StackNavigator react-navigation))
(def tab-navigator (.-TabNavigator react-navigation))
;;(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def picker (r/adapt-react-class (.-Picker ReactNative)))
(def picker-item (r/adapt-react-class (.-Picker.Item ReactNative)))

;; -- swiper start -------------------------------------------------------
(def swiper-type (.-Swiper (js/require "react-native-swiper")))

(def wrapper-style {})

(def slide-base-style
  {:flex           1
   :justifyContent "center"
   :alignItems     "center"})

(def slide1-style
  (assoc slide-base-style
         :backgroundColor "#9DD6EB"))

(def slide2-style
  (assoc slide-base-style
         :backgroundColor "#97CAE5"))

(def slide3-style
  (assoc slide-base-style
         :backgroundColor "#92BBD9"))

(def text-style
  {:color      "#fff"
   :fontSize   30
   :fontWeight "bold"})

(defn widget [data owner]
  (swiper {:style wrapper-style}
          (view {:style slide1-style}
                (text {:style text-style}
                      "Hello Swiper"))
          (view {:style slide2-style}
                (text {:style text-style}
                      "Beautiful"))
          (view {:style slide3-style}
                (text {:style text-style}
                      "And simple"))))
;; -- swiper end ---------------------------------------------------------

(def style
  {
   :title       {:font-size   30
                 :font-weight "100"
                 :margin      20
                 :text-align  "center"}
   :button      {:background-color "#999"
                 :padding          10
                 :margin-bottom    20
                 :border-radius    5}
   :button-text {:color       "white"
                 :text-align  "center"
                 :font-weight "bold"}
   })

(defn resd [props]
  (let [number (-> props (get "params") (get "number"))
        route-name "Index"]
    [view {:style {:align-items      "center"
                   :justify-content  "center"
                   :flex             1
                   :background-color (random-color)}}
     [view {:style {:background-color "rgba(256,256,256,0.5)"
                    :margin-bottom    20}}
      [text {:style (style :title)} "Card number " number]]
     [touchable-highlight
      {:style    (style :button)
       :on-press #(dispatch
                    [:nav/navigate
                     [#:nav.route {:key       (keyword (str number))
                                   :routeName :Card
                                   :params    {:number (inc number)}}
                      route-name]])}
      [text {:style (style :button-text)} "Next"]]
     [touchable-highlight {:on-press #(dispatch [:nav/reset route-name])
                           :style    (style :button)}
      [text {:style (style :button-text)} "RESET"]]]))

(defn settings []
  [view {:style {:flex 1
                 :justify-content "center"
                 :align-items "center"}}
   [text "SETTINGS"]])

(defn alert [title]
  (.alert Alert title))

(defn example-page []
  (let [greeting (subscribe [:get-greeting])
        textVal ""
        pickItems [[picker-item {:label "p1" :value "p1"}]
                   [picker-item {:label "p2" :value "p2"}]]]
    [view
     [text "Here be examples"]
     [text "sss"]

     [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
     [activity-indicator {:size :large :color :green}]
     [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                           :on-press
                           #(dispatch [:nav/navigate
                                       [#:nav.route {:key 0
                                                     :routeName :Card
                                                     :params {:number 1}}
                                        "Index"]])}

      [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]
     [input {:value textVal :on-change-text #(alert (str "entered=" %)) :placeholder "type something"}]
     [button {:title "clicky" :on-press #(alert "clickied")}]

     [text "picker"]
     [picker ]
     [text "scroll-view"]
     [scroll-view {:data [{:name "aaa" :idx 0 :key :a}
                        {:name "bbb" :idx 1 :key :b}
                        {:name "ccc" :idx 2 :key :c}]
                 :render-item (fn [x] (r/reactify-component [view  [text (:name x)]]))
                 :style {:height 15 :width 100 :color :red}
                 :removeClippedSubviews false

                   }]
     [text "flat-list"]
     [flat-list {:data [{:name "aaa" :idx 0 :key :a}
                                               {:name "bbb" :idx 1 :key :b}
                                               {:name "ccc" :idx 2 :key :c}]
                                        :render-item (fn [x] (r/reactify-component [view  [text (:name x)]]))
                                        :style {:height 15 :width 100 :color :red}
                                        :removeClippedSubviews false

                                        }]

     ]))

(defn app-root []
  (let [nav-state (subscribe [:nav/tab-state])
        st (r/reactify-component start)
        page (subscribe [:page])]
    (fn []
      [view {:style {:flex-direction "column" :margin 25 :align-items "center"}}


       ;;[status-bar {:hidden false :background-color :yellow :bar-style {:style "light-content"}}]
;;       [stackNav {[text "s-one"] {:screen [view [text "abc"]]}
;;                  [text "s-two"] {:screen [view [text "def"]]}}]




       [toolbar {:style {:height 40 :width 320 :background-color "#e3dd32"}
                 :logo logo-img
                 :title "lala"
                 :actions [{:title "home" :show "always"}
                           {:title "Config" :show "always"}
                           {:title "Card" :show "always"}
                           {:title "exampleS" :show "always"}
                           {:title "pdwduiwe"}]
                 :on-action-selected (fn [x] (dispatch [:page x]))}]
       (case @page
         0 [view [text "Home"]
            [image {:source (js/require "./assets/images/cljs.png")
                    :style {:width 50
                            :height 50}}]]
         1 [view [text "Config"]]
         2 [view [text "Card"]]
         3 (example-page)
         [view [text "?"]])

       ])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))


(defn nav-wrapper [component title]
  (let [comp (r/reactify-component
              (fn [{:keys [navigation]}]
                [component (-> navigation .-state js->clj)]))]
    (aset comp "navigationOptions" #js {"title" title})
    comp))


(def resd-comp (nav-wrapper resd #(str "Card "
                                       (aget % "state" "params" "number"))))

(def app-root-comp (nav-wrapper app-root "Welcome"))

(def stack-router {:Home {:screen app-root-comp}
                   :Card {:screen resd-comp}})


(def sn (r/adapt-react-class (stack-navigator (clj->js stack-router))))

(defn card-start [] (let [nav-state (subscribe [:nav/stack-state "Index"])]
                      (fn []
                        ;;(js/console.log @nav-state)
                        [sn {:navigation (add-navigation-helpers
                                           (clj->js
                                             {"dispatch" #(do
                                                            (js/console.log "EVENT" %)
                                                            (dispatch [:nav/js [% "Index"]]))
                                              "state"    (clj->js @nav-state)}))}])))

(def tab-router {:Index    {:screen (nav-wrapper card-start "Index")}
                 :Settings {:screen (nav-wrapper settings "Settings")}})



(defn tab-navigator-inst []
  (tab-navigator (clj->js tab-router) (clj->js {:order            ["Index" "Settings"]
                                                :initialRouteName "Index"})))

(defn get-state [action]
  (-> (tab-navigator-inst)
      .-router
      (.getStateForAction action)))

(defonce tn
  (let [tni (tab-navigator-inst)]
    (aset tni "router" "getStateForAction" #(let [new-state (get-state %)]
                                              (js/console.log "STATE" % new-state)
                                                             (dispatch [:nav/set new-state])
                                                             new-state) #_(do (js/console.log %)
                                                                              #_(get-state %)))
    (r/adapt-react-class tni)))

(defn start []
  (let [nav-state (subscribe [:nav/tab-state])]
    (fn []
      [tn])
    )
  )

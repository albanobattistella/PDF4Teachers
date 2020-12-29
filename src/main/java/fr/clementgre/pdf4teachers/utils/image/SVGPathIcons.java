package fr.clementgre.pdf4teachers.utils.image;

import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

public class SVGPathIcons {

    public static String PLUS = "M416 208H272V64c0-17.67-14.33-32-32-32h-32c-17.67 0-32 14.33-32 32v144H32c-17.67 0-32 14.33-32 32v32c0 17.67 14.33 32 32 32h144v144c0 17.67 14.33 32 32 32h32c17.67 0 32-14.33 32-32V304h144c17.67 0 32-14.33 32-32v-32c0-17.67-14.33-32-32-32z";
    public static String FORWARD_ARROWS = "M500.5 231.4l-192-160C287.9 54.3 256 68.6 256 96v320c0 27.4 31.9 41.8 52.5 24.6l192-160c15.3-12.8 15.3-36.4 0-49.2zm-256 0l-192-160C31.9 54.3 0 68.6 0 96v320c0 27.4 31.9 41.8 52.5 24.6l192-160c15.3-12.8 15.3-36.4 0-49.2z";
    public static String UNDO = "M212.333 224.333H12c-6.627 0-12-5.373-12-12V12C0 5.373 5.373 0 12 0h48c6.627 0 12 5.373 12 12v78.112C117.773 39.279 184.26 7.47 258.175 8.007c136.906.994 246.448 111.623 246.157 248.532C504.041 393.258 393.12 504 256.333 504c-64.089 0-122.496-24.313-166.51-64.215-5.099-4.622-5.334-12.554-.467-17.42l33.967-33.967c4.474-4.474 11.662-4.717 16.401-.525C170.76 415.336 211.58 432 256.333 432c97.268 0 176-78.716 176-176 0-97.267-78.716-176-176-176-58.496 0-110.28 28.476-142.274 72.333h98.274c6.627 0 12 5.373 12 12v48c0 6.627-5.373 12-12 12z";
    public static String REDO = "M500.33 0h-47.41a12 12 0 0 0-12 12.57l4 82.76A247.42 247.42 0 0 0 256 8C119.34 8 7.9 119.53 8 256.19 8.1 393.07 119.1 504 256 504a247.1 247.1 0 0 0 166.18-63.91 12 12 0 0 0 .48-17.43l-34-34a12 12 0 0 0-16.38-.55A176 176 0 1 1 402.1 157.8l-101.53-4.87a12 12 0 0 0-12.57 12v47.41a12 12 0 0 0 12 12h200.33a12 12 0 0 0 12-12V12a12 12 0 0 0-12-12z";
    public static String SCREEN_CORNERS = "M0 180V56c0-13.3 10.7-24 24-24h124c6.6 0 12 5.4 12 12v40c0 6.6-5.4 12-12 12H64v84c0 6.6-5.4 12-12 12H12c-6.6 0-12-5.4-12-12zM288 44v40c0 6.6 5.4 12 12 12h84v84c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12V56c0-13.3-10.7-24-24-24H300c-6.6 0-12 5.4-12 12zm148 276h-40c-6.6 0-12 5.4-12 12v84h-84c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h124c13.3 0 24-10.7 24-24V332c0-6.6-5.4-12-12-12zM160 468v-40c0-6.6-5.4-12-12-12H64v-84c0-6.6-5.4-12-12-12H12c-6.6 0-12 5.4-12 12v124c0 13.3 10.7 24 24 24h124c6.6 0 12-5.4 12-12z";

    public static String PDF_FILE = "M361.16 95.6844L279.242 13.7668C270.455 4.9795 258.543 0 246.143 0H46.8658C20.992 0.0976372 0 21.0896 0 46.9635V453.134C0 479.008 20.992 500 46.8658 500H328.061C353.935 500 374.927 479.008 374.927 453.134V128.881C374.927 116.481 369.947 104.472 361.16 95.6844ZM324.253 125.073H249.951V50.7713L324.253 125.073ZM46.8658 453.134V46.9635H203.085V148.506C203.085 161.492 213.533 171.939 226.518 171.939H328.061V453.134H46.8658ZM291.154 312.83C279.242 301.113 245.265 304.335 228.276 306.483C211.482 296.231 200.254 282.074 192.345 261.277C196.153 245.558 202.207 221.636 197.618 206.6C193.517 181.019 160.711 183.558 156.024 200.84C151.728 216.559 155.634 238.43 162.859 266.354C153.095 289.69 138.547 321.031 128.295 338.996C108.768 349.053 82.4058 364.577 78.5003 384.105C75.2783 399.531 103.886 438 152.802 353.642C174.673 346.417 198.496 337.532 219.586 334.017C238.039 343.976 259.617 350.615 274.068 350.615C298.965 350.615 301.406 323.081 291.154 312.83ZM97.7348 388.791C102.714 375.415 121.656 359.988 127.417 354.618C108.865 384.202 97.7348 389.475 97.7348 388.791ZM177.407 202.695C184.632 202.695 183.948 234.036 179.164 242.531C174.868 228.959 174.966 202.695 177.407 202.695ZM153.583 336.067C163.054 319.567 171.158 299.941 177.7 282.66C185.804 297.403 196.153 309.217 207.088 317.321C186.78 321.519 169.108 330.111 153.583 336.067ZM282.074 331.185C282.074 331.185 277.192 337.044 245.655 323.57C279.926 321.031 285.589 328.842 282.074 331.185Z";
    public static String TEXT_LETTER = "M437.25 1H63.75C58.2468 1 52.9689 2.88137 49.0775 6.23024C45.1862 9.5791 43 14.1211 43 18.8571L43 126C43 130.736 45.1862 135.278 49.0775 138.627C52.9689 141.976 58.2468 143.857 63.75 143.857H105.25C110.753 143.857 116.031 141.976 119.922 138.627C123.814 135.278 126 130.736 126 126V90.2857H198.625V429.571H146.75C141.247 429.571 135.969 431.453 132.078 434.802C128.186 438.151 126 442.693 126 447.429V483.143C126 487.879 128.186 492.421 132.078 495.77C135.969 499.119 141.247 501 146.75 501H354.25C359.753 501 365.031 499.119 368.922 495.77C372.814 492.421 375 487.879 375 483.143V447.429C375 442.693 372.814 438.151 368.922 434.802C365.031 431.453 359.753 429.571 354.25 429.571H302.375V90.2857H375V126C375 130.736 377.186 135.278 381.078 138.627C384.969 141.976 390.247 143.857 395.75 143.857H437.25C442.753 143.857 448.031 141.976 451.922 138.627C455.814 135.278 458 130.736 458 126V18.8571C458 14.1211 455.814 9.5791 451.922 6.23024C448.031 2.88137 442.753 1 437.25 1Z";
    public static String ON_TWENTY = "M287.646 366.34C290.951 366.29 294.14 367.602 296.513 369.989C298.885 372.377 300.247 375.643 300.299 379.071V414.267C300.247 417.695 298.885 420.962 296.513 423.349C294.14 425.736 290.951 427.049 287.646 426.998H161.645C158.185 427.04 154.753 426.337 151.568 424.934C148.383 423.53 145.513 421.457 143.139 418.845C140.765 416.233 138.94 413.139 137.777 409.758C136.614 406.376 136.14 402.782 136.384 399.201C140.612 343.639 158.695 308.633 194.86 255.058C225.653 209.397 235.902 196.402 236.072 157.706C236.074 157.376 236.075 157.045 236.075 156.712C236.075 130.019 233.352 118.424 217.505 118.424C203.225 118.424 198.206 130.098 198.206 154.203V162.454C198.18 164.153 197.832 165.83 197.181 167.389C196.531 168.948 195.59 170.36 194.414 171.542C193.237 172.724 191.848 173.654 190.325 174.279C188.802 174.904 187.175 175.212 185.537 175.185H147.669C146.031 175.212 144.404 174.904 142.881 174.279C141.358 173.654 139.968 172.724 138.792 171.542C137.616 170.36 136.675 168.948 136.024 167.389C135.374 165.83 135.026 164.153 135 162.454V156.033C135 112.965 147.897 61 220.821 61C286.216 61 305.333 101.78 305.333 157.232C305.333 213.363 286.414 238.02 253.093 285.505C235.512 309.169 215.665 341.62 212.927 366.277L287.646 366.34ZM500 326.538C500 382.006 481.294 426.998 412.963 426.998C346.685 426.998 329.667 380.428 329.667 326.901V160.735C329.667 96.4797 359.445 61 415.137 61C471.104 61 500 96.7164 500 157.706V326.538ZM414.636 120.522C400.325 120.522 394.515 132.354 394.515 152.074V334.994C394.515 355.676 399.975 367.476 414.636 367.476C429.296 367.476 434.406 354.856 434.406 333.921V151.017C434.437 133.237 429.859 120.522 414.636 120.522Z M49.3982 421.761L140.355 82.3054C141.642 77.5042 138.792 72.5692 133.991 71.2827L104.309 63.3294C99.5077 62.0429 94.5727 64.8921 93.2862 69.6933L2.32935 409.149C1.04287 413.95 3.89212 418.885 8.69331 420.172L38.3755 428.125C43.1767 429.411 48.1117 426.562 49.3982 421.761Z";
    public static String DRAW_POLYGON = "M428.571 357.143C428.181 357.143 427.824 357.254 427.433 357.254L383.683 284.353C389.342 274.118 392.857 262.522 392.857 250C392.857 237.478 389.353 225.882 383.683 215.647L427.433 142.746C427.824 142.757 428.181 142.857 428.571 142.857C468.025 142.857 500 110.882 500 71.4286C500 31.9754 468.025 0 428.571 0C402.199 0 379.42 14.4531 367.054 35.7143H132.946C120.58 14.4531 97.8013 0 71.4286 0C31.9754 0 0 31.9754 0 71.4286C0 97.8013 14.4531 120.58 35.7143 132.946V367.042C14.4531 379.42 0 402.199 0 428.571C0 468.025 31.9754 500 71.4286 500C97.8013 500 120.58 485.547 132.946 464.286H367.042C379.42 485.547 402.188 500 428.56 500C468.013 500 499.989 468.025 499.989 428.571C500 389.118 468.025 357.143 428.571 357.143ZM107.143 367.054V132.946C117.839 126.732 126.732 117.839 132.946 107.143H365.491L322.567 178.683C322.176 178.672 321.819 178.571 321.429 178.571C281.975 178.571 250 210.547 250 250C250 289.453 281.975 321.429 321.429 321.429C321.819 321.429 322.176 321.317 322.567 321.317L365.491 392.857H132.946C126.735 382.159 117.841 373.265 107.143 367.054ZM303.571 250C303.571 240.156 311.585 232.143 321.429 232.143C331.272 232.143 339.286 240.156 339.286 250C339.286 259.844 331.272 267.857 321.429 267.857C311.585 267.857 303.571 259.844 303.571 250ZM446.429 71.4286C446.429 81.2723 438.415 89.2857 428.571 89.2857C418.728 89.2857 410.714 81.2723 410.714 71.4286C410.714 61.5848 418.728 53.5714 428.571 53.5714C438.415 53.5714 446.429 61.5848 446.429 71.4286ZM71.4286 53.5714C81.2723 53.5714 89.2857 61.5848 89.2857 71.4286C89.2857 81.2723 81.2723 89.2857 71.4286 89.2857C61.5848 89.2857 53.5714 81.2723 53.5714 71.4286C53.5714 61.5848 61.5848 53.5714 71.4286 53.5714ZM53.5714 428.571C53.5714 418.728 61.5848 410.714 71.4286 410.714C81.2723 410.714 89.2857 418.728 89.2857 428.571C89.2857 438.415 81.2723 446.429 71.4286 446.429C61.5848 446.429 53.5714 438.415 53.5714 428.571ZM428.571 446.429C418.728 446.429 410.714 438.415 410.714 428.571C410.714 418.728 418.728 410.714 428.571 410.714C438.415 410.714 446.429 418.728 446.429 428.571C446.429 438.415 438.415 446.429 428.571 446.429Z";

    public static String SAVE = "M433.941 129.941l-83.882-83.882A48 48 0 0 0 316.118 32H48C21.49 32 0 53.49 0 80v352c0 26.51 21.49 48 48 48h352c26.51 0 48-21.49 48-48V163.882a48 48 0 0 0-14.059-33.941zM224 416c-35.346 0-64-28.654-64-64 0-35.346 28.654-64 64-64s64 28.654 64 64c0 35.346-28.654 64-64 64zm96-304.52V212c0 6.627-5.373 12-12 12H76c-6.627 0-12-5.373-12-12V108c0-6.627 5.373-12 12-12h228.52c3.183 0 6.235 1.264 8.485 3.515l3.48 3.48A11.996 11.996 0 0 1 320 111.48z";
    public static String SAVE_LITE = "M433.941 129.941l-83.882-83.882A48 48 0 0 0 316.118 32H48C21.49 32 0 53.49 0 80v352c0 26.51 21.49 48 48 48h352c26.51 0 48-21.49 48-48V163.882a48 48 0 0 0-14.059-33.941zM272 80v80H144V80h128zm122 352H54a6 6 0 0 1-6-6V86a6 6 0 0 1 6-6h42v104c0 13.255 10.745 24 24 24h176c13.255 0 24-10.745 24-24V83.882l78.243 78.243a6 6 0 0 1 1.757 4.243V426a6 6 0 0 1-6 6zM224 232c-48.523 0-88 39.477-88 88s39.477 88 88 88 88-39.477 88-88-39.477-88-88-88zm0 128c-22.056 0-40-17.944-40-40s17.944-40 40-40 40 17.944 40 40-17.944 40-40 40z";
    public static String LIST = "M48 48a48 48 0 1 0 48 48 48 48 0 0 0-48-48zm0 160a48 48 0 1 0 48 48 48 48 0 0 0-48-48zm0 160a48 48 0 1 0 48 48 48 48 0 0 0-48-48zm448 16H176a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h320a16 16 0 0 0 16-16v-32a16 16 0 0 0-16-16zm0-320H176a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h320a16 16 0 0 0 16-16V80a16 16 0 0 0-16-16zm0 160H176a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h320a16 16 0 0 0 16-16v-32a16 16 0 0 0-16-16z";
    public static String SORT = "M304 416h-64a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h64a16 16 0 0 0 16-16v-32a16 16 0 0 0-16-16zm-128-64h-48V48a16 16 0 0 0-16-16H80a16 16 0 0 0-16 16v304H16c-14.19 0-21.37 17.24-11.29 27.31l80 96a16 16 0 0 0 22.62 0l80-96C197.35 369.26 190.22 352 176 352zm256-192H240a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h192a16 16 0 0 0 16-16v-32a16 16 0 0 0-16-16zm-64 128H240a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h128a16 16 0 0 0 16-16v-32a16 16 0 0 0-16-16zM496 32H240a16 16 0 0 0-16 16v32a16 16 0 0 0 16 16h256a16 16 0 0 0 16-16V48a16 16 0 0 0-16-16z";

    public static String LEVEL_DOWN = "M313.553 392.331L209.587 504.334c-9.485 10.214-25.676 10.229-35.174 0L70.438 392.331C56.232 377.031 67.062 352 88.025 352H152V80H68.024a11.996 11.996 0 0 1-8.485-3.515l-56-56C-4.021 12.926 1.333 0 12.024 0H208c13.255 0 24 10.745 24 24v328h63.966c20.878 0 31.851 24.969 17.587 40.331z";

    public static String FOLDER = "M527.9 224H480v-48c0-26.5-21.5-48-48-48H272l-64-64H48C21.5 64 0 85.5 0 112v288c0 26.5 21.5 48 48 48h400c16.5 0 31.9-8.5 40.7-22.6l79.9-128c20-31.9-3-73.4-40.7-73.4zM48 118c0-3.3 2.7-6 6-6h134.1l64 64H426c3.3 0 6 2.7 6 6v42H152c-16.8 0-32.4 8.8-41.1 23.2L48 351.4zm400 282H72l77.2-128H528z";
    public static String TRASH = "M268 416h24a12 12 0 0 0 12-12V188a12 12 0 0 0-12-12h-24a12 12 0 0 0-12 12v216a12 12 0 0 0 12 12zM432 80h-82.41l-34-56.7A48 48 0 0 0 274.41 0H173.59a48 48 0 0 0-41.16 23.3L98.41 80H16A16 16 0 0 0 0 96v16a16 16 0 0 0 16 16h16v336a48 48 0 0 0 48 48h288a48 48 0 0 0 48-48V128h16a16 16 0 0 0 16-16V96a16 16 0 0 0-16-16zM171.84 50.91A6 6 0 0 1 177 48h94a6 6 0 0 1 5.15 2.91L293.61 80H154.39zM368 464H80V128h288zm-212-48h24a12 12 0 0 0 12-12V188a12 12 0 0 0-12-12h-24a12 12 0 0 0-12 12v216a12 12 0 0 0 12 12z";
    public static String CROSS = "M242.72 256l100.07-100.07c12.28-12.28 12.28-32.19 0-44.48l-22.24-22.24c-12.28-12.28-32.19-12.28-44.48 0L176 189.28 75.93 89.21c-12.28-12.28-32.19-12.28-44.48 0L9.21 111.45c-12.28 12.28-12.28 32.19 0 44.48L109.28 256 9.21 356.07c-12.28 12.28-12.28 32.19 0 44.48l22.24 22.24c12.28 12.28 32.2 12.28 44.48 0L176 322.72l100.07 100.07c12.28 12.28 32.2 12.28 44.48 0l22.24-22.24c12.28-12.28 12.28-32.19 0-44.48L242.72 256z";
    public static String EXPORT = "M296 384h-80c-13.3 0-24-10.7-24-24V192h-87.7c-17.8 0-26.7-21.5-14.1-34.1L242.3 5.7c7.5-7.5 19.8-7.5 27.3 0l152.2 152.2c12.6 12.6 3.7 34.1-14.1 34.1H320v168c0 13.3-10.7 24-24 24zm216-8v112c0 13.3-10.7 24-24 24H24c-13.3 0-24-10.7-24-24V376c0-13.3 10.7-24 24-24h136v8c0 30.9 25.1 56 56 56h80c30.9 0 56-25.1 56-56v-8h136c13.3 0 24 10.7 24 24zm-124 88c0-11-9-20-20-20s-20 9-20 20 9 20 20 20 20-9 20-20zm64 0c0-11-9-20-20-20s-20 9-20 20 9 20 20 20 20-9 20-20z";

    public static String PICTURES = "M480 416v16c0 26.51-21.49 48-48 48H48c-26.51 0-48-21.49-48-48V176c0-26.51 21.49-48 48-48h16v48H54a6 6 0 0 0-6 6v244a6 6 0 0 0 6 6h372a6 6 0 0 0 6-6v-10h48zm42-336H150a6 6 0 0 0-6 6v244a6 6 0 0 0 6 6h372a6 6 0 0 0 6-6V86a6 6 0 0 0-6-6zm6-48c26.51 0 48 21.49 48 48v256c0 26.51-21.49 48-48 48H144c-26.51 0-48-21.49-48-48V80c0-26.51 21.49-48 48-48h384zM264 144c0 22.091-17.909 40-40 40s-40-17.909-40-40 17.909-40 40-40 40 17.909 40 40zm-72 96l39.515-39.515c4.686-4.686 12.284-4.686 16.971 0L288 240l103.515-103.515c4.686-4.686 12.284-4.686 16.971 0L480 208v80H192v-48z";
    public static String EXCHANGE = "M0 168v-16c0-13.255 10.745-24 24-24h360V80c0-21.367 25.899-32.042 40.971-16.971l80 80c9.372 9.373 9.372 24.569 0 33.941l-80 80C409.956 271.982 384 261.456 384 240v-48H24c-13.255 0-24-10.745-24-24zm488 152H128v-48c0-21.314-25.862-32.08-40.971-16.971l-80 80c-9.372 9.373-9.372 24.569 0 33.941l80 80C102.057 463.997 128 453.437 128 432v-48h360c13.255 0 24-10.745 24-24v-16c0-13.255-10.745-24-24-24z";
    public static String IMPORT = "M216 0h80c13.3 0 24 10.7 24 24v168h87.7c17.8 0 26.7 21.5 14.1 34.1L269.7 378.3c-7.5 7.5-19.8 7.5-27.3 0L90.1 226.1c-12.6-12.6-3.7-34.1 14.1-34.1H192V24c0-13.3 10.7-24 24-24zm296 376v112c0 13.3-10.7 24-24 24H24c-13.3 0-24-10.7-24-24V376c0-13.3 10.7-24 24-24h146.7l49 49c20.1 20.1 52.5 20.1 72.6 0l49-49H488c13.3 0 24 10.7 24 24zm-124 88c0-11-9-20-20-20s-20 9-20 20 9 20 20 20 20-9 20-20zm64 0c0-11-9-20-20-20s-20 9-20 20 9 20 20 20 20-9 20-20z";
    public static String FULL_SCREEN = "M0 180V56c0-13.3 10.7-24 24-24h124c6.6 0 12 5.4 12 12v40c0 6.6-5.4 12-12 12H64v84c0 6.6-5.4 12-12 12H12c-6.6 0-12-5.4-12-12zM288 44v40c0 6.6 5.4 12 12 12h84v84c0 6.6 5.4 12 12 12h40c6.6 0 12-5.4 12-12V56c0-13.3-10.7-24-24-24H300c-6.6 0-12 5.4-12 12zm148 276h-40c-6.6 0-12 5.4-12 12v84h-84c-6.6 0-12 5.4-12 12v40c0 6.6 5.4 12 12 12h124c13.3 0 24-10.7 24-24V332c0-6.6-5.4-12-12-12zM160 468v-40c0-6.6-5.4-12-12-12H64v-84c0-6.6-5.4-12-12-12H12c-6.6 0-12 5.4-12 12v124c0 13.3 10.7 24 24 24h124c6.6 0 12-5.4 12-12z";
    public static String COMMAND_PROMPT = "M257.981 272.971L63.638 467.314c-9.373 9.373-24.569 9.373-33.941 0L7.029 444.647c-9.357-9.357-9.375-24.522-.04-33.901L161.011 256 6.99 101.255c-9.335-9.379-9.317-24.544.04-33.901l22.667-22.667c9.373-9.373 24.569-9.373 33.941 0L257.981 239.03c9.373 9.372 9.373 24.568 0 33.941zM640 456v-32c0-13.255-10.745-24-24-24H312c-13.255 0-24 10.745-24 24v32c0 13.255 10.745 24 24 24h304c13.255 0 24-10.745 24-24z";

    public static String SUN = "";
    public static String SU = "";


    public static Region generateImage(String path, String bgColor, int width, int height){
        return generateImage(path, bgColor, 0, width, height, 0, new int[]{1, 1}, null);
    }
    public static Region generateImage(String path, String bgColor, int padding, int width, int height){
        return generateImage(path, bgColor, padding, width, height, 0, new int[]{1, 1}, null);
    }
    public static Region generateImage(String path, String bgColor, int padding, int width, int height, ColorAdjust effect){
        return generateImage(path, bgColor, padding, width, height, 0, new int[]{1, 1}, effect);
    }
    public static Region generateImage(String path, String bgColor, int padding, int width, int height, int rotate){
        return generateImage(path, bgColor, padding, width, height, rotate, new int[]{1, 1}, null);
    }
    public static Region generateImage(String path, String bgColor, int padding, int width, int height, int rotate, int[] ratio){
        return generateImage(path, bgColor, padding, width, height, rotate, ratio, null);
    }
    public static Region generateImage(String path, String bgColor, int padding, int width, int height, int rotate, int[] ratio, ColorAdjust effect){

        SVGPath image = new SVGPath();
        image.setContent(path);
        image.setStyle("-fx-margin: 20px;");

        Region imageRegion = new Region();
        imageRegion.setShape(image);
        imageRegion.setRotate(rotate);
        imageRegion.setStyle("-fx-background-color: " + bgColor + ";");

        if(effect != null){
            effect.brightnessProperty().addListener((observable, oldValue, newValue) -> {
                imageRegion.setEffect(effect);
            });
            imageRegion.setEffect(effect);
        }

        StackPane imagePane = new StackPane();
        if(width != 0 && height != 0){
            int w = width - padding*2;


            if(ratio[0] == 1 && ratio[1] == 1 && image.getLayoutBounds().getWidth() != image.getLayoutBounds().getHeight() && width == height){
                double rw = image.getLayoutBounds().getWidth();
                double rh = image.getLayoutBounds().getHeight();
                if(rw < rh){
                    imageRegion.setMinWidth(w/rh*rw);
                    imageRegion.setMinHeight(w);
                    double dif = (w - w/rh*rw) / 2.0;
                    imagePane.setPadding(new Insets(0, dif, 0, dif));
                }else{
                    imageRegion.setMinWidth(w);
                    imageRegion.setMinHeight(w/rw*rh);
                    double dif = (w - w/rw*rh) / 2;
                    imagePane.setPadding(new Insets(dif, 0, dif,0));
                }
                imagePane.getChildren().add(imageRegion);
                return imagePane;
            }else{
                imageRegion.setMinWidth(w);
                imageRegion.setMinHeight(w / ((double) width) * height);
                return imageRegion;
            }
        }else{

            int w = width - padding*2;
            int h = height - padding*2;

            if(height == 0){
                h = (int) (w / ((double) ratio[0]) * ratio[1]);
                imagePane.setPadding(new Insets((w-h)/2d, 0, (w-h)/2d, 0));
            }else{
                w = (int) (h / ((double) ratio[1]) * ratio[0]);
                imagePane.setPadding(new Insets(0, (h-w)/2d, 0, (h-w)/2d));
            }


            imageRegion.setMinWidth(w);
            imageRegion.setMinHeight(h);
            imagePane.getChildren().add(imageRegion);
            return imagePane;
        }

    }

}

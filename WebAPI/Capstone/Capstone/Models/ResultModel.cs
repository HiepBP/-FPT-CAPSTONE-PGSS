﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models
{
    public class ResultModel
    {
        public bool success { get; set; }
        public string message { get; set; }
        public object obj { get; set; }
    }
}